package com.kyoko.myalbum.File;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.google.gson.Gson;
import com.kyoko.myalbum.DAO.PhotoRepo;
import com.kyoko.myalbum.Entity.Photo;
import com.kyoko.myalbum.Enum.EnumCode;
import com.kyoko.myalbum.Exception.MyException;
import com.kyoko.myalbum.Outdated.MyFileNotFoundException;
import com.kyoko.myalbum.Outdated.ResultUtil;
import com.kyoko.myalbum.Property.ProjProperties;
import com.kyoko.myalbum.Result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

/**
 * @author young
 * @create 2023/5/6 13:46
 * @Description
 */
@Service
public class FileStorageServ {
    private final Path fileStorageLocation;
    private final PhotoRepo photoRepo;


    @Autowired
    public FileStorageServ(ProjProperties properties, PhotoRepo photoRepo) {
        //改写为绝对路径，如果没有盘符则默认为C盘？,不能创建Users下的目录？
        this.fileStorageLocation = Paths.get(properties.getFileStoragePath())
                .toAbsolutePath().normalize();
        this.photoRepo = photoRepo;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new MyException(
                    Result.builder()
                            .code(EnumCode.FILE_IO_ERROR.getValue())
                            .msg("无法创建目录，请尝试更换其他路径并重启系统！")
                            .data(e)
                            .build().toJson());
            //ResultUtil.result(EnumCode.FILE_IO_ERROR.getValue(), "无法创建目录，请尝试更换其他路径并重启系统！"));
        }
    }

    //图片名称是否合法，true是
    public boolean isValid(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        return !fileName.contains("..");
    }

    //判断文件是否为图片,true是
    public boolean isImage(MultipartFile file) {
        String fileType = file.getContentType();
        return fileType.startsWith("image");
    }

    //提取图片元数据
    public HashMap<String, HashMap<String, String>> extractExif(MultipartFile file) {
        HashMap<String, HashMap<String,String>> exifMap = new HashMap<>();

        try {
            exifMap = new ExifUtil(file.getInputStream()).getExifMap();
            //metadata = ImageMetadataReader.readMetadata(file.getInputStream());
        } catch (ImageProcessingException e) {
            throw new MyException(
                    Result.builder()
                            .code(EnumCode.FILE_FAIL_EXTRACT.getValue())
                            .msg("图片处理失败，请重试！")
                            .build().toJson());
            //ResultUtil.result(EnumCode.FILE_FAIL_EXTRACT.getValue(), "图片处理失败！", e));
        } catch (IOException e) {
            throw new MyException(
                    Result.builder()
                            .code(EnumCode.FILE_IO_ERROR.getValue())
                            .msg("图片读写失败，请重试！")
                            .build().toJson());
            //ResultUtil.result(EnumCode.FILE_IO_ERROR.getValue(), "图片读写失败！", e));
        }

        return exifMap;
    }

    //存储图片到文件系统并生成photo对象
    public RespFile storeFile(MultipartFile file, Integer uid) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileType = file.getContentType();
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));//.jpg 类似的文件后缀

       if (fileName.contains(".."))
           return RespFile.builder()
                   .isSuccess(false)
                   .fileName(fileName)
                   .fileType(fileType)
                   .msg("文件名包含非法字符！")
                   .size(file.getSize())
                   .build();
        if (!fileType.startsWith("image"))
            return RespFile.builder()
                    .isSuccess(false)
                    .fileName(fileName)
                    .fileType(fileType)
                    .size(file.getSize())
                    .msg("不支持的文件类型，请上传图片！")
                    .build();

        HashMap<String, HashMap<String, String>> exifMap = extractExif(file);
        String exifJson = new Gson().toJson(exifMap);
        //写入photo对象，方便管理
        Photo ToAddPhoto = Photo.builder()
                .photoName(fileName)
                .ownerID(uid)
                .exif(exifJson)
                .build();

        //获取生成的photo对象，根据pid设置保存位置
        Photo saved = photoRepo.save(ToAddPhoto);
        String pid = saved.getPid();
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("file/downloadFile/")
                .path(pid + fileSuffix)
                .toUriString();
        RespFile respFile = RespFile.builder()
                .isSuccess(true)
                .fileName(fileName)
                .fileType(fileType)
                .fileDownloadUri(fileDownloadUri)
                .size(file.getSize())
                .exif(exifMap)
                //.metadata(metadata)
                .build();


        Path targetLocation = this.fileStorageLocation.resolve(pid+fileSuffix);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            saved.setPhotoPath(fileDownloadUri);
        } catch (IOException e) {
            //写入失败则回滚
            photoRepo.delete(saved);
            throw new MyException(

                    Result.builder()
                            .code(EnumCode.FILE_IO_ERROR.getValue())
                            .msg("图片读写失败，请重试！")
                            .build().toJson());
            //ResultUtil.result(EnumCode.FILE_IO_ERROR.getValue(), "图片读写失败！", e));
        }
        return respFile;
    }

    //
    public Resource loadFileAsResource(String pid) {
        try {
            Path filePath = this.fileStorageLocation.resolve(pid).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + pid);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + pid, ex);
        }
    }
}
