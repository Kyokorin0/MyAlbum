package com.kyoko.myalbum.Service.IMPL;

import com.kyoko.myalbum.DAO.MyUserRepo;
import com.kyoko.myalbum.Entity.MyUser;
import com.kyoko.myalbum.Service.MAPPER.UserServ;
import com.kyoko.myalbum.record.ReqUser;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author young
 * @create 2023/3/11 23:59
 * @Description
 */
@Service
public class UserServImpl implements UserServ {
    private final MyUserRepo userRepo;

    //@Autowired等效构造函数
    public UserServImpl(MyUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<MyUser> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public void addUser(ReqUser reqUser) {
        //todo
        MyUser toAddMyUser = MyUser.builder()
                .email(reqUser.email())
                .password(reqUser.password())
                .nickname(reqUser.nickname())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
        userRepo.save(toAddMyUser);
    }
}
