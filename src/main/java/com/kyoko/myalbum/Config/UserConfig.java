package com.kyoko.myalbum.Config;

import com.kyoko.myalbum.DAO.UserRepo;
import com.kyoko.myalbum.Entity.User;
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
public class UserConfig {
    @Bean
    CommandLineRunner user(UserRepo userRepo) {
        return args -> {
            User user1 = new User();
            User user2 = new User();

            user1.setUid(10003);//无效，主键自动生成
            user1.setUsername("FirstU");
            user1.setPassword("123456");
            user1.setNickname("FirstU");

            user2.setUsername("SecondU");
            user2.setPassword("123456");
            user2.setNickname("SecondU");

            userRepo.saveAll(List.of(user1, user2));
        };

    }
}
