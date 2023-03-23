package com.kyoko.myalbum.record;

/**
 * @author young
 * @create 2023/3/12 0:14
 * @Description
 */
public record ReqUser(
        String email,
        String password,
        String nickname
) {}
