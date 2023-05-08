package com.kyoko.myalbum.auth;

import com.kyoko.myalbum.enumCode.EnumCode;
import com.kyoko.myalbum.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author young
 * @create 2023/3/15 1:06
 * @Description
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthCon {

    private final AuthServ service;
    @PostMapping("/register")
    public Object register(@RequestBody RegisterRequest request) {
        String regResp = service.register(request);
        return Result.builder()
                .code(EnumCode.OK.getValue())
                .msg("注册请求成功！请前往邮箱确认！")
                .data(request.getEmail())
                .build().toJson();
    }
    @GetMapping("/confirm")
    public Object confirm(@RequestParam("token") String token){
        AuthResp authResp = service.confirmToken(token);
        return Result.builder()
                .code(EnumCode.OK.getValue())
                .msg("邮箱验证成功！")
                .data(authResp)
                .build().toJson();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResp> authenticate(@RequestBody AuthReq request) {

        return ResponseEntity.ok(service.authenticate(request));
    }
}
