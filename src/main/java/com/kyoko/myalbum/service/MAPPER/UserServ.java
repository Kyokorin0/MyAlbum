package com.kyoko.myalbum.service.MAPPER;

import com.kyoko.myalbum.entity.MyUser;
import com.kyoko.myalbum.record.ReqUser;

import java.util.List;

/**
 * @author young
 * @create 2023/3/12 0:02
 * @Description
 */
public interface UserServ {
    List<MyUser> getUsers();

    void addUser(ReqUser reqUser);
}
