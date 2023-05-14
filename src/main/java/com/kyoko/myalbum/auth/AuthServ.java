package com.kyoko.myalbum.auth;

import com.kyoko.myalbum.dao.MyUserRepo;
import com.kyoko.myalbum.email.EmailSender;
import com.kyoko.myalbum.entity.Confirmation;
import com.kyoko.myalbum.entity.MyUser;
import com.kyoko.myalbum.enumCode.EnumCode;
import com.kyoko.myalbum.enumCode.Role;
import com.kyoko.myalbum.exception.MyException;
import com.kyoko.myalbum.filter.Util.JwtTokenUtil;
import com.kyoko.myalbum.properties.ProjProperties;
import com.kyoko.myalbum.record.UserInfo;
import com.kyoko.myalbum.result.Result;
import com.kyoko.myalbum.service.IMPL.ConfirmServImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * @author young
 * @create 2023/3/15 1:24
 * @Description
 */
@Service
@RequiredArgsConstructor
public class AuthServ {
    private final ProjProperties projProperties;
    private final EmailValidator emailValidator;
    private final MyUserRepo repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final ConfirmServImpl confirmServ;
    private final EmailSender emailSender;

    public String register(RegisterRequest request) {
        //验证邮箱是否有效
        boolean isValid = emailValidator.test(request.getEmail());
        Integer uid = null;
        if (!isValid) {
            throw new MyException(Result.builder()
                    .code(EnumCode.BAD_REQUEST.getValue())
                    .msg("邮箱格式错误！")
                    .data(request.getEmail())
                    .build().toJson());
        }
        //验证邮箱是否已被注册
        Optional<MyUser> findUser = repo.findByEmail(request.getEmail());
        if(findUser.isPresent()&&findUser.get().isEnabled()){
            throw new MyException(Result.builder()
                    .code(EnumCode.FORBIDDEN.getValue())
                    .msg("该邮箱已注册，请直接登录！")
                    .data(request.getEmail())
                    .build().toJson());
        }
        if(findUser.isPresent())
            uid  = findUser.get().getUid();
        MyUser myUser = MyUser.builder()
                .uid(uid)
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(false)
                .build();
        repo.save(myUser);
        //用于邮箱验证的token
        String token = UUID.randomUUID().toString();
        Confirmation confirmation = Confirmation.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(projProperties.getConfirmTokenExpiration()))
                .myUser(myUser)
                .build();
        confirmServ.saveConfirmation(confirmation);
        //String generateToken = jwtTokenUtil.generateToken(myUser);
        //直接拼接？会被编码为%3f，用queryParam来设置参数
        String link = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/v1/auth/confirm")
                .queryParam("token",token)
                .toUriString();
        emailSender.send(request.getEmail(),buildEmail(request.getNickname(),link));
        return new HashMap<String,String>().put("link",link);
    }

    public AuthResp confirmToken(String token) {
        Confirmation confirmation = confirmServ
                .getConfirmation(token)
                .orElseThrow(() -> new MyException(Result.builder()
                        .code(EnumCode.BAD_REQUEST.getValue())
                        .msg("该token不存在！")
                        .data(token)
                        .build()
                        .toJson()
                ));
        if (confirmation.getConfirmedAt() != null) {
            throw new MyException(Result.builder()
                    .code(EnumCode.FORBIDDEN.getValue())
                    .msg("邮箱已通过验证，无需再次验证！")
                    .build()
                    .toJson()
            );
        }
        MyUser myUser = confirmation.getMyUser();
        String jwt = jwtTokenUtil.generateToken(myUser);
        confirmServ.setConfirmedAt(token);
        repo.enableMyUser(myUser.getEmail());
        return AuthResp.builder()
                .token(jwt)
                .userInfo(new UserInfo(
                        myUser.getUid(),
                        myUser.getNickname(),
                        myUser.getEmail()))
                .build();

    }

    public AuthResp authenticate(AuthReq request) {
        //先验证用户是否存在
        Optional<MyUser> findUser = repo.findByEmail(request.getEmail());
        if (!findUser.isPresent()){
            throw new MyException(Result.builder()
                    .code(EnumCode.NOT_USER.getValue())
                    .msg("该邮箱未注册，请先注册！")
                    .data(request.getEmail())
                    .build().toJson());
        }else if(!findUser.get().isEnabled()){
            throw new MyException(Result.builder()
                    .code(EnumCode.NOT_USER.getValue())
                    .msg("该邮箱未激活，请前往邮箱激活或重新注册！")
                    .data(request.getEmail())
                    .build().toJson());
        }else {
            try {
                //验证账户名和密码，不匹配会抛出异常
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
            } catch (AuthenticationException e) {
                    throw new MyException(Result.builder()
                            .code(EnumCode.LOGIN_FAIL.getValue())
                            .msg("用户名或密码错误！")
                            .data(e.getMessage())
                            .build()
                            .toJson());
            }
            String generateToken = jwtTokenUtil.generateToken(findUser.get());
            return AuthResp.builder()
                    .token(generateToken)
                    .userInfo(new UserInfo(
                            findUser.get().getUid(),
                            findUser.get().getNickname(),
                            findUser.get().getEmail()))
                    .build();
        }
    }
    //邮件模板
    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">确认你的邮箱</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">您好 " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> 你正在注册MyAlbum，请点击下面的按钮来激活你的账户: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">点击激活</a> </p></blockquote>\n 链接将在15分钟后过期. <p>感谢您注册！</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}
