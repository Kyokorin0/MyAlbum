package com.kyoko.myalbum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author young
 * @create 2023/5/8 20:37
 * @Description
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Confirmation {
 @Id
 @SequenceGenerator(
         name = "confirm_token_sequence",
         sequenceName = "confirm_token_sequence",
         allocationSize = 1
 )
 @GeneratedValue(
         strategy = GenerationType.SEQUENCE,
         generator = "confirm_token_sequence"
 )
 private Long id;
 @Column(nullable = false)
 private String token;
 @Column(nullable = false)
 private LocalDateTime createdAt;
 private LocalDateTime expiresAt;
 private LocalDateTime confirmedAt;
 @ManyToOne
 @JoinColumn(nullable = false,name = "my_user_id")
 private MyUser myUser;
}
