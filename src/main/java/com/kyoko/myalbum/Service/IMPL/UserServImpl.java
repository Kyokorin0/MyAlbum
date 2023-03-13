package com.kyoko.myalbum.Service.IMPL;

import com.kyoko.myalbum.DAO.UserRepo;
import com.kyoko.myalbum.Entity.User;
import com.kyoko.myalbum.Service.MAPPER.UserServ;
import com.kyoko.myalbum.record.ReqUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author young
 * @create 2023/3/11 23:59
 * @Description
 */
@Service
public class UserServImpl implements UserServ {
    private final UserRepo userRepo;

    //@Autowired等效构造函数
    public UserServImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public void addUser(ReqUser reqUser) {
        User ToAddUser = new User();
        ToAddUser.setUsername(reqUser.username());
        ToAddUser.setNickname(reqUser.nickname());
        ToAddUser.setPassword(reqUser.password());
        userRepo.save(ToAddUser);
    }
}
