package com.kyoko.myalbum.Filter.Util;

import com.kyoko.myalbum.DAO.MyUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author young
 * @create 2023/3/14 16:38
 * @Description
 */
@Service
@RequiredArgsConstructor
public class MyUserDetailsUtil implements org.springframework.security.core.userdetails.UserDetailsService {

    private final MyUserRepo myUserRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return myUserRepo
                .findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }
    //可以创建，但未使用,也未实现
    public UserDetails loadUserByMyEmail(String myEmail) throws UsernameNotFoundException {
        return null;
    }
}
