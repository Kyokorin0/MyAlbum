package com.kyoko.myalbum.Auth;

import com.kyoko.myalbum.DAO.MyUserRepo;
import com.kyoko.myalbum.Entity.MyUser;
import com.kyoko.myalbum.Enum.Role;
import com.kyoko.myalbum.Filter.Util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author young
 * @create 2023/3/15 1:24
 * @Description
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final MyUserRepo repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        MyUser myUser = MyUser.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
        repo.save(myUser);
        String generateToken = jwtTokenUtil.generateToken(myUser);
        return AuthenticationResponse.builder()
                .token(generateToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );//验证账户名和密码，不匹配会抛出异常
        MyUser myUser = repo.findByEmail(request.getEmail())
                .orElseThrow();//其他异常
        String generateToken = jwtTokenUtil.generateToken(myUser);
        return AuthenticationResponse.builder()
                .token(generateToken)
                .build();
    }
}
