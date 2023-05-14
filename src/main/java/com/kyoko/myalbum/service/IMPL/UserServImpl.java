package com.kyoko.myalbum.service.IMPL;

import com.kyoko.myalbum.dao.MyUserRepo;
import com.kyoko.myalbum.entity.MyUser;
import com.kyoko.myalbum.entity.Photo;
import com.kyoko.myalbum.service.MAPPER.UserServ;
import com.kyoko.myalbum.record.ReqUser;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                .build();
        userRepo.save(toAddMyUser);
    }
    @Override
    //sortBy是Photo表的属性
    public Optional<MyUser> findUserByID(Integer uid) {
        return userRepo.findById(uid);
    }
}
