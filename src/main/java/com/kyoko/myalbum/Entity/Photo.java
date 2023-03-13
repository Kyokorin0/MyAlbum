package com.kyoko.myalbum.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;

/**
 * @author young
 * @create 2023/3/11 23:09
 * @Description
 */
//多行编辑 ctrl alt shift lmb
@Entity
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
    private String exif;//json?
    private Boolean shared;//yes-true no-false default-false

    public Photo() {

    }

    @Override
    public String toString() {
        return "Photo{" +
                "pid='" + pid + '\'' +
                ", md5='" + md5 + '\'' +
                ", photoName='" + photoName + '\'' +
                ", photoPath='" + photoPath + '\'' +
                ", ownerID=" + ownerID +
                ", exif='" + exif + '\'' +
                ", shared=" + shared +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(pid, photo.pid) && Objects.equals(md5, photo.md5) && Objects.equals(photoName, photo.photoName) && Objects.equals(photoPath, photo.photoPath) && Objects.equals(ownerID, photo.ownerID) && Objects.equals(exif, photo.exif) && Objects.equals(shared, photo.shared);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, md5, photoName, photoPath, ownerID, exif, shared);
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public String getExif() {
        return exif;
    }

    public void setExif(String exif) {
        this.exif = exif;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Photo(String photoName, String pid, String md5, String photoPath, Integer ownerUid, String exif, Boolean shared) {
        this.photoName = photoName;
        this.pid = pid;
        this.md5 = md5;
        this.photoPath = photoPath;
        this.ownerID = ownerUid;
        this.exif = exif;
        this.shared = shared;
    }
}
