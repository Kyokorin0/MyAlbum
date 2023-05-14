package com.kyoko.myalbum.controller;

import com.kyoko.myalbum.entity.MyUser;
import com.kyoko.myalbum.entity.Photo;
import com.kyoko.myalbum.enumCode.EnumCode;
import com.kyoko.myalbum.exception.MyException;
import com.kyoko.myalbum.record.ReqPhoto;
import com.kyoko.myalbum.result.Result;
import com.kyoko.myalbum.service.MAPPER.PhotoServ;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author young
 * @create 2023/5/10 19:00
 * @Description
 */
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/user/photos")
public class CommonPCon {
    private final PhotoServ photoServ;

    @PostMapping("/my")
    public String getPhotos(@RequestParam("pageNumber") int page,
                                 @RequestParam("pageSize") int size) {
        SecurityContext context = SecurityContextHolder.getContext();
        MyUser user = (MyUser) context.getAuthentication().getPrincipal();
        //user.getUid()
        Page<Photo> pagePhotosByUid = photoServ.getPagePhotosByUid(page, size, user.getUid());
        return Result.builder()
                .code(EnumCode.OK.getValue())
                .msg("查询分页图片请求成功！")
                .data(pagePhotosByUid)
                .build().toJson();
    }
    @PostMapping("/shared")
    public String getSharedP(@RequestParam("pageNumber") int page,
                             @RequestParam("pageSize") int size) {
        SecurityContext context = SecurityContextHolder.getContext();
        MyUser user = (MyUser) context.getAuthentication().getPrincipal();
        //user.getUid()
        Page<Photo> pagePhotosByExample =
                photoServ.getPagePhotosByExample(page, size, Photo.builder()
                                .shared(true)
                                .ownerID(user.getUid())
                        .build());
        return Result.builder()
                .code(EnumCode.OK.getValue())
                .msg("查询已分享图片请求成功！")
                .data(pagePhotosByExample)
                .build().toJson();
    }



    @PostMapping()
    public void addPhoto(@RequestBody() ReqPhoto req) {
        photoServ.addPhoto(req);
    }
    @PostMapping("/del")
    public String delPhotoByPid(@RequestBody ReqPhoto reqPhoto){
        SecurityContext context = SecurityContextHolder.getContext();
        MyUser user = (MyUser) context.getAuthentication().getPrincipal();
        Optional<Photo> photoByID = photoServ.findPhotoByID(reqPhoto.pid());
        if(photoByID.isPresent()){
            Photo photo = photoByID.get();
            if(!photo.getOwnerID().equals(user.getUid()))
                throw new MyException(Result.builder()
                        .code(EnumCode.FORBIDDEN.getValue())
                        .msg("非法操作，不是你拥有的图片！")
                        .data(reqPhoto)
                        .build().toJson());
            photoServ.delPhotoByPid(photo.getPid());
            return Result.builder()
                    .code(EnumCode.OK.getValue())
                    .msg("删除成功！")
                    .data(photo)
                    .build().toJson();
        }
        else{
            return Result.builder()
                    .code(EnumCode.OK.getValue())
                    .msg("图片已被删除！")
                    .data(reqPhoto)
                    .build().toJson();
        }
    }
}
