package com.kyoko.myalbum.service.IMPL;

import com.kyoko.myalbum.dao.PhotoRepo;
import com.kyoko.myalbum.entity.Photo;
import com.kyoko.myalbum.service.MAPPER.PhotoServ;
import com.kyoko.myalbum.record.ReqPhoto;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
                .createAt(new Date(System.currentTimeMillis()))
                .build();

        photoRepo.save(ToAddPhoto);
    }
    @Override
    //sortBy是Photo表的属性
    public Page<Photo> getPagePhotosByUid(int pageNumber, int pageSize, Integer uid) {
        //通过pid进行倒序，pid 是Bean 中的变量，不是数据库中的字段（*）
        Sort sort = Sort.by(Sort.Direction.DESC, "pid");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        //注意用作example的实体类属性需要包装，boolean int date不包装可能报错
        Photo examPhoto = new Photo();
        examPhoto.setOwnerID(uid);
        //精确匹配examPhoto的ownerId属性
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher(
                "ownerID", matcher -> matcher.exact());
        Example<Photo> example = Example.of(examPhoto, exampleMatcher);
        return photoRepo.findAll(example,pageRequest);
        //photoRepo.find
    }

    @Override
    public Optional<Photo> findPhotoByID(String pid){
        return photoRepo.findById(pid);
    }
    @Override
    public Photo savePhoto(Photo photo){
        return photoRepo.save(photo);
    }

    @Override
    public List<Photo> findPhotoByExample(Photo photo){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher(
                "ownerID", matcher -> matcher.exact());
        Example<Photo> example = Example.of(photo, exampleMatcher);
        return photoRepo.findAll(example);
    }

    @Override
    //sortBy是Photo表的属性
    public Page<Photo> getPagePhotosByExample(int pageNumber, int pageSize, Photo photo) {
        //通过pid进行倒序，pid 是Bean 中的变量，不是数据库中的字段（*）
        Sort sort = Sort.by(Sort.Direction.DESC, "pid");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher(
                "ownerID", matcher -> matcher.exact());
        Example<Photo> example = Example.of(photo, exampleMatcher);
        return photoRepo.findAll(example,pageRequest);
        //photoRepo.find
    }
    @Override
    public void delPhotoByPid(String pid){
        photoRepo.deleteById(pid);
    }

}
