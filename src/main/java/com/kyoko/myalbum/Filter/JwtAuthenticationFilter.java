package com.kyoko.myalbum.Filter;

import com.kyoko.myalbum.Entity.MyUser;
import com.kyoko.myalbum.Enum.EnumCode;
import com.kyoko.myalbum.Exception.MyException;
import com.kyoko.myalbum.Filter.Util.JwtTokenUtil;
import com.kyoko.myalbum.Filter.Util.MyUserDetailsUtil;
import com.kyoko.myalbum.Result.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author young
 * @create 2023/3/14 1:11
 * @Description Jwt验证过滤器继承自一次性过滤器，也可以选择实现filter接口
 * 验证JwtToken，简单处理admin的权限控制
 */
@Component//将jwt过滤器交由spring容器管理，@Service、@Repository在这里作用相同，都是扩展自@Component
@RequiredArgsConstructor//生成由final字段为参数的构造函数
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final MyUserDetailsUtil myUserDetailsUtil;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            //首先获取请求头中的授权信息Authorization
            final String AuthHeader = request.getHeader("Authorization");
            String uri = request.getRequestURI();//admin路径访问需要ADMIN角色
            final String JwtToken;
            final String UserEmail;//用于登录验证的username，这里是MyUser.Email
            //验证非空、token是否是BearerToken格式
            if (AuthHeader == null || !AuthHeader.startsWith("Bearer ")) {
                //token为空，或者不是Bearer交由下个过滤器处理
                filterChain.doFilter(request, response);
                return;
                //throw new MyException(ResultUtil.result(EnumCode.UNAUTHORIZED.getValue(), "非法token"));
            }

            //从AuthHeader第7位开始JwtToken，前7位是“Bearer ”，注意包含空格
            JwtToken = AuthHeader.substring(7);
            UserEmail = jwtTokenUtil.extractUsername(JwtToken);//从Jwt中提取username,这里是email
            //验证账户名不为空，而且未经过校验，经过校验则直接放行
            if (UserEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //验证userDetails是否与数据库中的MyUser匹配
                MyUser userDetails = (MyUser) this.myUserDetailsUtil.loadUserByUsername(UserEmail);//本项目采用UserEmail作为username
                //if语句验证token是否有效，包括一致性和时效性
                if (jwtTokenUtil.isTokenValid(JwtToken, userDetails)) {
                    //更新SecurityContextHolder，并将请求发送到DispatcherServlet
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    //spring用来更新SecurityContextHolder
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);


                    //简单的权限验证，admin路径访问控制
                    String role = userDetails.getRole().name();
                    if (uri.startsWith("/api/v1/admin/") && !role.equals("ADMIN")) {
                        throw new MyException(Result.builder()
                                .code(EnumCode.UNAUTHORIZED.getValue())
                                .msg("用户权限不足")
                                .build().toJson());
                        //ResultUtil.result(EnumCode.UNAUTHORIZED.getValue(), "用户权限不足"));
                    }
                } else {
                    throw new MyException(Result.builder()
                            .code(EnumCode.UNAUTHORIZED.getValue())
                            .msg("失效token")
                            .build().toJson());
                    //ResultUtil.result(EnumCode.UNAUTHORIZED.getValue(), "失效token"));
                }
            }
            //最后将请求递给下个过滤器
            filterChain.doFilter(request, response);
        }
        //统一处理的异常
        catch (MyException e) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getResult().toString());
        }
        //由this.myUserDetailsUtil.loadUserByUsername(UserEmail)抛出
        catch (UsernameNotFoundException e) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(
                    Result.builder()
                            .code(EnumCode.UNAUTHORIZED.getValue())
                            .msg("找不到用户信息")
                            .build().toJson()
                    //ResultUtil.result(EnumCode.EXCPTION_ERROR.getValue(), "找不到用户信息")
            );
        }
        //由jwtTokenUtil.extractUsername(JwtToken)抛出
        catch (Exception e) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(
                    Result.builder()
                            .code(EnumCode.UNAUTHORIZED.getValue())
                            .msg(e.getMessage())
                            .data(e)
                            .build().toJson()
                    //ResultUtil.result(EnumCode.EXCPTION_ERROR.getValue(), "非法token", e.getMessage())
            );
        }
    }
}
