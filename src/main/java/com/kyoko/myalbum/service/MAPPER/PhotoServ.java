package com.kyoko.myalbum.service.MAPPER;

import com.kyoko.myalbum.entity.Photo;
import com.kyoko.myalbum.record.ReqPhoto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author young
 * @create 2023/3/12 0:32
 * @Description
 */
public interface PhotoServ {
    List<Photo> getPhotos();
    void addPhoto(ReqPhoto reqPhoto);

    //sortBy是Photo表的属性
    Page<Photo> getPagePhotosByUid(int pageNumber, int pageSize,Integer uid);

    Optional<Photo> findPhotoByID(String pid);

    Photo savePhoto(Photo photo);

    List<Photo> findPhotoByExample(Photo photo);

    //sortBy是Photo表的属性
    Page<Photo> getPagePhotosByExample(int pageNumber, int pageSize, Photo photo);

    void delPhotoByPid(String pid);
}
