package com.kyoko.myalbum.service.IMPL;

import com.kyoko.myalbum.dao.SharedRepo;
import com.kyoko.myalbum.entity.Shared;
import com.kyoko.myalbum.service.MAPPER.SharedServ;
import com.kyoko.myalbum.record.ReqShared;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
