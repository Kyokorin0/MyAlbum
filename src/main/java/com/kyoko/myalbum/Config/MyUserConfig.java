package com.kyoko.myalbum.Config;

import com.kyoko.myalbum.DAO.MyUserRepo;
import com.kyoko.myalbum.Entity.MyUser;
import com.kyoko.myalbum.Enum.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author young
 * @create 2023/3/12 16:02
 * @Description
 */
@Configuration
public class MyUserConfig {
    @Bean
    CommandLineRunner user(MyUserRepo userRepo) {
        return args -> {
            MyUser myUser1 = new MyUser();
            MyUser myUser2 = new MyUser();
            MyUser buildU = MyUser.builder()
                    .email("BuildU@email.com")
                    .nickname("BuildU")
                    .password("123456")
                    .role(Role.ADMIN)
                    .build();

            myUser1.setUid(10003);//无效，主键自动生成
            myUser1.setPassword("123456");
            myUser1.setNickname("FirstU");
            myUser1.setEmail("10001@email.com");
            myUser1.setRole(Role.ADMIN);


            myUser2.setPassword("123456");
            myUser2.setNickname("SecondU");
            myUser2.setEmail("10002@email.com");
            myUser2.setRole(Role.ADMIN);

            userRepo.saveAll(List.of(myUser1, myUser2,buildU));
        };

    }
}
