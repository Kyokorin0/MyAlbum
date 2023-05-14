package com.kyoko.myalbum.service.MAPPER;

import com.kyoko.myalbum.entity.Shared;
import com.kyoko.myalbum.record.ReqShared;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author young
 * @create 2023/3/12 17:35
 * @Description
 */
public interface SharedServ {
    List<Shared> getShared();
    void addShared(ReqShared reqShared);

    void saveShared(Shared shared);

    List<Shared> findShared(ReqShared reqShared);

    //sortBy是Photo表的属性
    Page<Shared> getPageSharedByOwner(int pageNumber, int pageSize, Integer oid);

    void delSharedByPAid(String paid);

    Optional<Shared> findSharedByID(String paid);

    Page<Shared> getPageSharedByAuth(int pageNumber, int pageSize, Integer authid);
}
