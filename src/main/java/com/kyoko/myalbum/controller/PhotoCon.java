package com.kyoko.myalbum.controller;

import com.kyoko.myalbum.entity.Photo;
import com.kyoko.myalbum.record.ReqPhoto;
import com.kyoko.myalbum.service.MAPPER.PhotoServ;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author young
 * @create 2023/3/12 0:41
 * @Description
 */
@RestController
@RequestMapping("api/v1/admin/photos")
public class PhotoCon {
    private final PhotoServ photoServ;

    public PhotoCon(PhotoServ photoServ) {
        this.photoServ = photoServ;
    }

    @GetMapping()
    public List<Photo> getPhotos() {
        return photoServ.getPhotos();
    }


    @PostMapping()
    public void addPhoto(@RequestBody() ReqPhoto req) {
        photoServ.addPhoto(req);
    }
}
