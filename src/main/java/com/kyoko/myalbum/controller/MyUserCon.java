package com.kyoko.myalbum.controller;

import com.kyoko.myalbum.entity.MyUser;
import com.kyoko.myalbum.service.IMPL.UserServImpl;
import com.kyoko.myalbum.record.ReqUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author young
 * @create 2023/3/11 23:39
 * @Description
 */
@RestController
@RequestMapping("api/v1/admin/users")
public class MyUserCon {
    private final UserServImpl userServ;

    public MyUserCon(UserServImpl userServ) {
        this.userServ = userServ;
    }


    @GetMapping()
    public List<MyUser> getUsers(@RequestHeader("Authorization")String auth) {
        return userServ.getUsers();
    }



    @PostMapping()
    public void addUser(@RequestBody() ReqUser req) {
        userServ.addUser(req);
    }

}
