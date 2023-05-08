package com.kyoko.myalbum.service.MAPPER;

import com.kyoko.myalbum.entity.Confirmation;

import java.util.Optional;

/**
 * @author young
 * @create 2023/5/8 23:23
 * @Description
 */
public interface ConfirmServ {
    void saveConfirmation(Confirmation confirmation);

    Optional<Confirmation> getConfirmation(String token);

    void setConfirmedAt(String token);
}
