package com.kyoko.myalbum.dao;

import com.kyoko.myalbum.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
