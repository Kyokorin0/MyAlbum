package com.kyoko.myalbum.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * @author young
 * @create 2023/3/11 23:09
 * @Description
 */
//多行编辑 ctrl alt shift lmb
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@NoArgsConstructor//生成无参构造函数，支持@Entity和@Builder
@Builder//支持链式构造对象,需要显式定义无参构造函数
public class Photo {
    @Id
    @UuidGenerator(
            style = UuidGenerator.Style.TIME
    )
    @GeneratedValue(
            strategy = GenerationType.UUID,
            generator = "photo_id_generator"
    )//自生成string类型主键，关于时间
    private String pid;
    private String md5;
    private String photoName;
    private String photoPath;
    private Integer ownerID;//foreign
    @Column(name = "exifJson",columnDefinition = "json")
    private String exif;//json?
    private Boolean shared;//yes-true no-false default-false
    //创建时间
    private Date createAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Photo photo = (Photo) o;
        return pid != null && Objects.equals(pid, photo.pid);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
