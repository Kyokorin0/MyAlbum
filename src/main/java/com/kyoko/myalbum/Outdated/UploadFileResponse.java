package com.kyoko.myalbum.Outdated;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author young
 * @create 2023/5/2 16:50
 * @Description
 */
@Data
@AllArgsConstructor
public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}