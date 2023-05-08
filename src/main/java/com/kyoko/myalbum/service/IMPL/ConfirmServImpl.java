package com.kyoko.myalbum.service.IMPL;

import com.kyoko.myalbum.dao.ConfirmRepo;
import com.kyoko.myalbum.entity.Confirmation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author young
 * @create 2023/5/8 21:10
 * @Description
 */
@Service
@AllArgsConstructor
public class ConfirmServImpl implements com.kyoko.myalbum.service.MAPPER.ConfirmServ {
    private final ConfirmRepo confirmRepo;
    @Override
    public void saveConfirmation(Confirmation confirmation){
        confirmRepo.save(confirmation);
    }

    @Override
    public Optional<Confirmation> getConfirmation(String token){
        return confirmRepo.findByToken(token);
    }

    @Override
    public void setConfirmedAt(String token){
        confirmRepo.updateConfirmedAt(token, LocalDateTime.now());
    }
}
