package com.kyoko.myalbum.Outdated;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.kyoko.myalbum.Enum.EnumCode;
import com.kyoko.myalbum.Exception.MyException;
import com.kyoko.myalbum.Property.ProjProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @Description 原始代码，根据具体业务需求已重构
 */

public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired

    public FileStorageService(ProjProperties projProperties) {
        //改写为绝对路径，如果没有盘符则默认为C盘？,不能创建Users下的目录？
        this.fileStorageLocation = Paths.get(projProperties.getFileStoragePath())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new MyException(ResultUtil.result(EnumCode.INTERNAL_SERVER_ERROR.getValue(), "无法创建目录，请尝试更换其他路径并重启系统！"));
        }
    }

    //判断文件是否为图片,true是
    public boolean isImage(MultipartFile file) {
        String fileType = file.getContentType();
        return fileType.startsWith("image");
    }

    //提取图片元数据
    public Metadata extractExif(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        boolean image = isImage(file);
        Metadata metadata = null;
        if (image) {
            try {
                metadata = ImageMetadataReader.readMetadata(file.getInputStream());
            } catch (ImageProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new MyException(ResultUtil.result(EnumCode.INTERNAL_SERVER_ERROR.getValue(), "文件解析失败！", fileName));
            }
        }
        return metadata;
    }

    public String storeFile(MultipartFile file) throws MyException {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileType = file.getContentType();
        //todo file.getContentType()过滤非图片文件
        //todo copy后extractExif，photoService创建photo实体方便管理
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new MyException(ResultUtil.result(EnumCode.INTERNAL_SERVER_ERROR.getValue(), "文件名包含非法字符！", fileName));
            }
            if (!fileType.startsWith("image")) {
                throw new MyException(ResultUtil.result(EnumCode.BAD_REQUEST.getValue(), "不支持的文件类型，请上传图片！", fileName));
            }

            Metadata metadata = ImageMetadataReader.readMetadata(file.getInputStream());

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        } catch (ImageProcessingException e) {
            throw new MyException(ResultUtil.result(EnumCode.INTERNAL_SERVER_ERROR.getValue(), "图片处理失败！", e));
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
