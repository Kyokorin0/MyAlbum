package com.kyoko.myalbum.Service.MAPPER;

import com.kyoko.myalbum.Entity.User;
import com.kyoko.myalbum.record.ReqUser;

import java.util.List;

/**
 * @author young
 * @create 2023/3/12 0:02
 * @Description
 */
public interface UserServ {
    List<User> getUsers();

    void addUser(ReqUser reqUser);
}
