package com.kyoko.myalbum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.Objects;


/**
 * @author young
 * @create 2023/3/11 23:16
 * @Description 联系User和Photo
 */
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@NoArgsConstructor//生成无参构造函数，支持@Entity和@Builder
@Builder//支持链式构造对象,需要显式定义无参构造函数
public class Shared {
    @Id
    @UuidGenerator(
            style = UuidGenerator.Style.TIME
    )
    @GeneratedValue(
            strategy = GenerationType.UUID,
            generator = "shared_id_generator"
    )//自生成string类型主键，关于时间
    private String PAid;//Photo-AuthedUser primary
    private String photoID;//foreign？
    private String photoPath;
    private String photoName;
    private String message;
    private Date shareAt;
    private Integer ownerID;
    private Integer authID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Shared shared = (Shared) o;
        return PAid != null && Objects.equals(PAid, shared.PAid);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
