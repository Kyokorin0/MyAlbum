package com.kyoko.myalbum.record;

/**
 * @author young
 * @create 2023/3/12 17:41
 * @Description
 */
public record ReqShared(
        String paID,
        String photoID,//foreign？
        Integer ownerID,
        Integer authID,
        String message
) {
}
