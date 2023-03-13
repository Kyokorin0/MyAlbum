package com.kyoko.myalbum.DAO;

import com.kyoko.myalbum.Entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author young
 * @create 2023/3/11 23:37
 * @Description
 */
@Repository
public interface PhotoRepo extends JpaRepository<Photo,String> {
}
