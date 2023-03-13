package com.kyoko.myalbum.Config;

import com.kyoko.myalbum.DAO.PhotoRepo;
import com.kyoko.myalbum.Entity.Photo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author young
 * @create 2023/3/12 16:29
 * @Description
 */
@Configuration
public class PhotoConfig {
    @Bean
    CommandLineRunner photo(PhotoRepo photoRepo) {
        return args -> {
            Photo Photo1 = new Photo();
            Photo Photo2 = new Photo();

            Photo1.setPid("PhotoId1");//无效
            Photo1.setPhotoName("FirstPhoto");
            Photo1.setMd5("defaultMD5");
            Photo1.setOwnerID(10000);
            Photo1.setPhotoPath("defaultPath");
            Photo1.setExif("defaultExif");
            Photo1.setShared(false);

            Photo2.setPid("PhotoId2");//无效
            Photo2.setPhotoName("SecondPhoto");
            Photo2.setMd5("defaultMD5");
            Photo2.setOwnerID(10001);
            Photo2.setPhotoPath("defaultPath");
            Photo2.setExif("defaultExif");
            Photo2.setShared(false);

            photoRepo.saveAll(List.of(Photo1, Photo2));

        };
    }
}
