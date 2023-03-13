package com.kyoko.myalbum.Service.MAPPER;

import com.kyoko.myalbum.Entity.Photo;
import com.kyoko.myalbum.record.ReqPhoto;

import java.util.List;

/**
 * @author young
 * @create 2023/3/12 0:32
 * @Description
 */
public interface PhotoServ {
    List<Photo> getPhotos();
    void addPhoto(ReqPhoto reqPhoto);
}
