package com.kyoko.myalbum.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author young
 * @create 2023/3/15 1:18
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String password;
    private String nickname;
    private String email;
}
