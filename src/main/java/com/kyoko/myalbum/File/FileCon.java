package com.kyoko.myalbum.File;

import com.kyoko.myalbum.Enum.EnumCode;
import com.kyoko.myalbum.Result.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
public class FileCon {

    private static final Logger logger = LoggerFactory.getLogger(FileCon.class);


    private final FileStorageServ fileStorageServ;

    public FileCon(FileStorageServ fileStorageServ) {
        this.fileStorageServ = fileStorageServ;
    }

    @PostMapping("/uploadFile")
    public Object uploadFile(@RequestParam("file") MultipartFile file) {
        //todo 验证uid
        RespFile respFile = fileStorageServ.storeFile(file, 10001);


        return Result.builder()
                .code(EnumCode.OK.getValue())
                .msg("请求成功！")
                .data(respFile)//metadata直接使用的话文件太大会导致错误
                .build().toJson();
        //ResultUtil.result(EnumCode.OK.getValue(), "上传成功！",resp);
    }

    @PostMapping("/uploadMultipleFiles")
    public Object uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestHeader HttpHeaders headers) {
        List<Object> collect = Arrays.asList(files)
                .stream()
                .map(file -> fileStorageServ.storeFile(file, 10001))
                .collect(Collectors.toList());
        return Result.builder()
                .code(EnumCode.OK.getValue())
                .msg("请求成功！")
                .data(collect)
                .build().toJson();
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    //生成限时的下载链接，通过uri-创建时间表管理，限制创建时间的24小时内使用
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageServ.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
