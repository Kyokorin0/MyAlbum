package com.kyoko.myalbum.Config;

import com.kyoko.myalbum.DAO.SharedRepo;
import com.kyoko.myalbum.Entity.Shared;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author young
 * @create 2023/3/12 17:54
 * @Description
 */
@Configuration
public class SharedConfig {
 @Bean
 CommandLineRunner shared(SharedRepo sharedRepo){
  return args -> {
   Shared shared1 = new Shared();
   Shared shared2 = new Shared();

   shared1.setPhotoID("PhotoId1");
   shared1.setOwnerID(10000);
   shared1.setAuthID(10001);

   shared2.setPhotoID("PhotoId2");
   shared2.setOwnerID(10001);
   shared2.setAuthID(10000);

   sharedRepo.saveAll(List.of(shared1,shared2));
  };
 }
}
