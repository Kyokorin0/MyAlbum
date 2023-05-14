package com.kyoko.myalbum.auth;

import com.kyoko.myalbum.record.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author young
 * @create 2023/3/15 1:15
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResp {
 private String token;
 private UserInfo userInfo;
}
