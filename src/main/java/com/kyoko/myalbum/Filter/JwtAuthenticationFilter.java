package com.kyoko.myalbum.Filter;

import com.kyoko.myalbum.Filter.Util.JwtTokenUtil;
import com.kyoko.myalbum.Filter.Util.MyUserDetailsUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author young
 * @create 2023/3/14 1:11
 * @Description Jwt验证过滤器继承自一次性过滤器，也可以选择实现filter接口
 * 验证JwtToken
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
        //首先获取请求头中的授权信息Authorization
        final String AuthHeader = request.getHeader("Authorization");
        final String JwtToken;
        final String UserEmail;//用于登录验证的username，这里是MyUser.Email
        //验证非空、token是否是BearerToken格式
        if(AuthHeader==null||!AuthHeader.startsWith("Bearer ")){
            //token为空，或者不是Bearer交由下个过滤器处理
            filterChain.doFilter(request,response);
            return;
        }
        //从AuthHeader第7位开始JwtToken，前7位是“Bearer ”，注意包含空格
        JwtToken = AuthHeader.substring(7);
        UserEmail = jwtTokenUtil.extractUsername(JwtToken);//从Jwt中提取username,这里是email
        //验证账户名不为空，而且未经过校验，经过校验则直接放行
        if (UserEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //验证userDetails是否与数据库中的MyUser匹配
            UserDetails userDetails = this.myUserDetailsUtil.loadUserByUsername(UserEmail);//本项目采用UserEmail作为username
            if(jwtTokenUtil.isTokenValid(JwtToken,userDetails)){
                //更新SecurityContextHolder，并将请求发送到DispatcherServlet
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );//spring用来更新SecurityContextHolder
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        //最后将请求递给下个过滤器
        filterChain.doFilter(request,response);
    }
}
