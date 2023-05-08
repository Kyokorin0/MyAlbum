package com.kyoko.myalbum.Service.IMPL;

import com.kyoko.myalbum.DAO.PhotoRepo;
import com.kyoko.myalbum.Entity.Photo;
import com.kyoko.myalbum.Service.MAPPER.PhotoServ;
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
    private PhotoRepo photoRepo;

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
