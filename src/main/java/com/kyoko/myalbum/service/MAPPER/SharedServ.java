package com.kyoko.myalbum.service.MAPPER;

import com.kyoko.myalbum.entity.Shared;
import com.kyoko.myalbum.record.ReqShared;

import java.util.List;

/**
 * @author young
 * @create 2023/3/12 17:35
 * @Description
 */
public interface SharedServ {
    List<Shared> getShared();
    void addShared(ReqShared reqShared);
}
