package com.kyoko.myalbum.controller;

import com.kyoko.myalbum.dao.PhotoRepo;
import com.kyoko.myalbum.entity.MyUser;
import com.kyoko.myalbum.entity.Photo;
import com.kyoko.myalbum.entity.Shared;
import com.kyoko.myalbum.enumCode.EnumCode;
import com.kyoko.myalbum.exception.MyException;
import com.kyoko.myalbum.record.ReqPhoto;
import com.kyoko.myalbum.record.ReqShared;
import com.kyoko.myalbum.result.Result;
import com.kyoko.myalbum.service.IMPL.PhotoServImpl;
import com.kyoko.myalbum.service.IMPL.UserServImpl;
import com.kyoko.myalbum.service.MAPPER.SharedServ;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

/**
 * @author young
 * @create 2023/5/13 14:34
 * @Description
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user/share")
public class CommonSCon {
    private final SharedServ sharedServ;
    private final UserServImpl userServ;
    private final PhotoServImpl photoServ;

    //    @PostMapping("/my")
//    public String getShared(){
//        SecurityContext context = SecurityContextHolder.getContext();
//        MyUser user = (MyUser) context.getAuthentication().getPrincipal();
//
//    }
    @PostMapping("/add")
    public String addShared(@RequestBody ReqShared reqShared) {
        SecurityContext context = SecurityContextHolder.getContext();
        MyUser user = (MyUser) context.getAuthentication().getPrincipal();
        //判断是否存在
        if(!sharedServ.findShared(reqShared).isEmpty())
            throw new MyException(Result.builder()
                    .code(EnumCode.OK.getValue())
                    .msg("已经分享过了！")
                    .data(reqShared)
                    .build().toJson());
        Optional<Photo> photoByID = photoServ.findPhotoByID(reqShared.photoID());
        //user.getUid()
        if(!userServ.findUserByID(reqShared.authID()).isPresent())
            throw new MyException(Result.builder()
                    .code(EnumCode.BAD_REQUEST.getValue())
                    .msg("目标用户不存在")
                    .data(reqShared.authID())
                    .build().toJson());
        if(!photoByID.isPresent()) {
            throw new MyException(Result.builder()
                    .code(EnumCode.BAD_REQUEST.getValue())
                    .msg("目标图片不存在")
                    .data(reqShared.photoID())
                    .build().toJson());
        }
        if(user.getUid().equals(reqShared.authID())){
            throw new MyException(Result.builder()
                    .code(EnumCode.BAD_REQUEST.getValue())
                    .msg("不能分享给自己！")
                    .data(reqShared.authID())
                    .build().toJson());
        }
        else{
            Photo photo = photoByID.get();
            if(!photo.getOwnerID().equals(user.getUid()))
                throw new MyException(Result.builder()
                        .code(EnumCode.FORBIDDEN.getValue())
                        .msg("非法操作，不是你拥有的图片！")
                        .data(reqShared.photoID())
                        .build().toJson());
            Shared build = Shared.builder()
                    .photoID(photo.getPid())
                    .photoPath(photo.getPhotoPath())
                    .photoName(photo.getPhotoName())
                    .ownerID(user.getUid())
                    .authID(reqShared.authID())
                    .message(reqShared.message())
                    .shareAt(new Date(System.currentTimeMillis()))
                    .build();
            sharedServ.saveShared(build);
            photo.setShared(true);
            photoServ.savePhoto(photo);
            return Result.builder()
                    .code(EnumCode.OK.getValue())
                    .msg("分享成功！")
                    .data(reqShared)
                    .build().toJson();
        }
    }

    @PostMapping("/my")
    public String getShared(@RequestParam("pageNumber") int page,
                            @RequestParam("pageSize") int size) {
        SecurityContext context = SecurityContextHolder.getContext();
        MyUser user = (MyUser) context.getAuthentication().getPrincipal();
        //user.getUid()
        Page<Shared> pageSharedByOwner =
                sharedServ.getPageSharedByOwner(page, size, user.getUid());
        return Result.builder()
                .code(EnumCode.OK.getValue())
                .msg("查询分享照片记录请求成功！")
                .data(pageSharedByOwner)
                .build()
                .toJson();
    }

    @PostMapping("/del")
    public String delPhotoByPid(@RequestBody ReqShared reqShared){
        SecurityContext context = SecurityContextHolder.getContext();
        MyUser user = (MyUser) context.getAuthentication().getPrincipal();
        Optional<Shared> sharedByID = sharedServ.findSharedByID(reqShared.paID());
        if(sharedByID.isPresent()){
            Shared shared = sharedByID.get();
            //操作者必须是分享关系中的一方
            boolean valid = shared.getOwnerID().equals(user.getUid())
                    || shared.getAuthID().equals(user.getUid());
            if(!valid)
                throw new MyException(Result.builder()
                        .code(EnumCode.FORBIDDEN.getValue())
                        .msg("非法操作，不是关于你的分享！")
                        .data(reqShared)
                        .build().toJson());
            sharedServ.delSharedByPAid(shared.getPAid());
            return Result.builder()
                    .code(EnumCode.OK.getValue())
                    .msg("删除成功！")
                    .data(shared)
                    .build().toJson();
        }
        else{
            return Result.builder()
                    .code(EnumCode.OK.getValue())
                    .msg("分享已被删除！")
                    .data(reqShared)
                    .build().toJson();
        }
    }

    @PostMapping("/tome")
    public String getSharedTome(@RequestParam("pageNumber") int page,
                            @RequestParam("pageSize") int size) {
        SecurityContext context = SecurityContextHolder.getContext();
        MyUser user = (MyUser) context.getAuthentication().getPrincipal();
        //user.getUid()
        Page<Shared> pageSharedByAuth = sharedServ.getPageSharedByAuth(page, size, user.getUid());
        return Result.builder()
                .code(EnumCode.OK.getValue())
                .msg("查询收到的照片请求成功！")
                .data(pageSharedByAuth)
                .build()
                .toJson();
    }
}
