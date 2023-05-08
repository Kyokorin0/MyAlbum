package com.kyoko.myalbum.dao;

import com.kyoko.myalbum.entity.Shared;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author young
 * @create 2023/3/11 23:38
 * @Description
 */
@Repository
public interface SharedRepo extends JpaRepository<Shared,String> {
}
