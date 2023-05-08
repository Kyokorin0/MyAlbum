package com.kyoko.myalbum.dao;

import com.kyoko.myalbum.entity.Confirmation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author young
 * @create 2023/5/8 21:06
 * @Description
 */
@Repository
public interface ConfirmRepo extends JpaRepository<Confirmation,Long> {
 Optional<Confirmation> findByToken(String token);
 @Transactional
 @Modifying
 @Query("UPDATE Confirmation c " +
         "SET c.confirmedAt = ?2 " +
         "WHERE c.token = ?1")
 void updateConfirmedAt(String token,
                       LocalDateTime confirmedAt);
}
