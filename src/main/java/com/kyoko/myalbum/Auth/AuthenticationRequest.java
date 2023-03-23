package com.kyoko.myalbum.Auth;

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
public class AuthenticationRequest {
 private String email;
 private String password;//todo
}
