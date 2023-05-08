package com.kyoko.myalbum.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author young
 * @create 2023/3/15 1:20
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthReq {
 private String email;
 private String password;//todo
}
