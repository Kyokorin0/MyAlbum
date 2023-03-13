package com.kyoko.myalbum.Controller;

import com.kyoko.myalbum.DAO.UserRepo;
import com.kyoko.myalbum.Entity.User;
import com.kyoko.myalbum.Service.IMPL.UserServImpl;
import com.kyoko.myalbum.record.ReqUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author young
 * @create 2023/3/11 23:39
 * @Description
 */
@RestController
@RequestMapping("api/v1/users")
public class UserCon {
    private final UserServImpl userServ;

    public UserCon(UserServImpl userServ) {
        this.userServ = userServ;
    }


    @GetMapping()
    public List<User> getUsers() {
        return userServ.getUsers();
    }



    @PostMapping()
    public void addUser(@RequestBody() ReqUser req) {
        userServ.addUser(req);
    }

}
