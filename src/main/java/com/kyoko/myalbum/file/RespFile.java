package com.kyoko.myalbum.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * @author young
 * @create 2023/5/6 13:42
 * @Description
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespFile{
        private boolean isSuccess;
        private String msg;
        private String fileName;
        private String fileDownloadUri;
        private String fileType;
        private long size;
        private Object exif;
}