package com.kyoko.myalbum.DAO;

import com.kyoko.myalbum.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author young
 * @create 2023/3/11 23:35
 * @Description
 */
@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
}
