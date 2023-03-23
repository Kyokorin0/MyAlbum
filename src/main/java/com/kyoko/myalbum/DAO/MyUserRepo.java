package com.kyoko.myalbum.DAO;

import com.kyoko.myalbum.Entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author young
 * @create 2023/3/11 23:35
 * @Description
 */
@Repository//支持对MyUser表的读写
public interface MyUserRepo extends JpaRepository<MyUser,Integer> {
    Optional<MyUser> findByEmail(String email);
}
