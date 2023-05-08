package com.kyoko.myalbum.dao;

import com.kyoko.myalbum.entity.MyUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Transactional
    @Modifying
    @Query("UPDATE MyUser a " +
            "SET a.isEnabled = TRUE WHERE a.email = ?1")
    void enableMyUser(String email);
    //int enableMyUser(String email);
}
