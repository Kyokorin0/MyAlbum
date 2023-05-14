package com.kyoko.myalbum.service.IMPL;

import com.kyoko.myalbum.dao.SharedRepo;
import com.kyoko.myalbum.entity.Photo;
import com.kyoko.myalbum.entity.Shared;
import com.kyoko.myalbum.service.MAPPER.SharedServ;
import com.kyoko.myalbum.record.ReqShared;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author young
 * @create 2023/3/12 17:45
 * @Description
 */
@Service
public class SharedServImpl implements SharedServ {
    private final SharedRepo sharedRepo;

    public SharedServImpl(SharedRepo sharedRepo) {
        this.sharedRepo = sharedRepo;
    }

    @Override
    public List<Shared> getShared() {
        return sharedRepo.findAll();
    }

    @Override
    public void addShared(ReqShared reqShared) {
        Shared ToAddShared = new Shared();
        ToAddShared.setPhotoID(reqShared.photoID());
        ToAddShared.setOwnerID(reqShared.ownerID());
        ToAddShared.setAuthID(reqShared.authID());
        sharedRepo.save(ToAddShared);

    }
    @Override
    public void saveShared(Shared shared){
        sharedRepo.save(shared);
    }
    @Override
    public List<Shared> findShared(ReqShared reqShared){
        Shared examShared = Shared.builder()
                .photoID(reqShared.photoID())
                .ownerID(reqShared.ownerID())
                .authID(reqShared.authID())
                .build();
        //精确匹配examPhoto的ownerId属性
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher(
                "ownerID", matcher -> matcher.exact());
        Example<Shared> example = Example.of(examShared, exampleMatcher);
        return sharedRepo.findAll(example);
    }
    @Override
    //sortBy是Shared表的属性
    public Page<Shared> getPageSharedByOwner(int pageNumber, int pageSize, Integer oid) {
        //通过pid进行倒序，pid 是Bean 中的变量，不是数据库中的字段（*）
        Sort sort = Sort.by(Sort.Direction.DESC, "PAid");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        //注意用作example的实体类属性需要包装，boolean int date不包装可能报错
        Shared examShared = new Shared();
        examShared.setOwnerID(oid);
        //精确匹配examPhoto的ownerId属性
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher(
                "ownerID", matcher -> matcher.exact());
        Example<Shared> example = Example.of(examShared, exampleMatcher);
        return sharedRepo.findAll(example,pageRequest);
        //photoRepo.find
    }

    @Override
    public void delSharedByPAid(String paid){
        sharedRepo.deleteById(paid);
    }
    @Override
    public Optional<Shared> findSharedByID(String paid){
        return sharedRepo.findById(paid);
    }
@Override
    public Page<Shared> getPageSharedByAuth(int pageNumber, int pageSize, Integer authid) {
        //通过pid进行倒序，pid 是Bean 中的变量，不是数据库中的字段（*）
        Sort sort = Sort.by(Sort.Direction.DESC, "PAid");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        //注意用作example的实体类属性需要包装，boolean int date不包装可能报错
        Shared examShared = new Shared();
        examShared.setAuthID(authid);
        //精确匹配examPhoto的ownerId属性
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher(
                "ownerID", matcher -> matcher.exact());
        Example<Shared> example = Example.of(examShared, exampleMatcher);
        return sharedRepo.findAll(example,pageRequest);
        //photoRepo.find
    }

}
