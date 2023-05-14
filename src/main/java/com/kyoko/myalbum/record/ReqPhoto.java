package com.kyoko.myalbum.record;

/**
 * @author young
 * @create 2023/3/12 0:34
 * @Description
 */
public record ReqPhoto(
        String pid,
        String photoName,
        String md5,
        String photoPath,
        Integer ownerID,//foreign
        String exif,//json?
        Boolean shared) {
}
