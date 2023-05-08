package com.kyoko.myalbum.service.IMPL;

import com.kyoko.myalbum.dao.PhotoRepo;
import com.kyoko.myalbum.entity.Photo;
import com.kyoko.myalbum.service.MAPPER.PhotoServ;
import com.kyoko.myalbum.record.ReqPhoto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author young
 * @create 2023/3/12 0:37
 * @Description
 */
@Service
public class PhotoServImpl implements PhotoServ {
    private final PhotoRepo photoRepo;

    public PhotoServImpl(PhotoRepo photoRepo) {
        this.photoRepo = photoRepo;
    }

    @Override
    public List<Photo> getPhotos() {
        return photoRepo.findAll();
    }

    @Override
    public void addPhoto(ReqPhoto reqPhoto) {
        //exif_json由uploadFileServ写入
        Photo ToAddPhoto = Photo.builder()
                .pid(reqPhoto.pid())
                .photoName(reqPhoto.photoName())
                .ownerID(reqPhoto.ownerID())
                .md5(reqPhoto.md5())
                .photoPath(reqPhoto.photoPath())
                .shared(reqPhoto.shared())
                .build();
        photoRepo.save(ToAddPhoto);
    }



}
