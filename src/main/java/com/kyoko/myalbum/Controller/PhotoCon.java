package com.kyoko.myalbum.Controller;

import com.kyoko.myalbum.Entity.Photo;
import com.kyoko.myalbum.Service.MAPPER.PhotoServ;
import com.kyoko.myalbum.record.ReqPhoto;
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
    private PhotoServ photoServ;

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
