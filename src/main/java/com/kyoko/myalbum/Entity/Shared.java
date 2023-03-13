package com.kyoko.myalbum.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;

/**
 * @author young
 * @create 2023/3/11 23:16
 * @Description 联系User和Photo
 */
@Entity
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
    private Integer ownerID;
    private Integer authID;

    public Shared() {

    }

    @Override
    public String toString() {
        return "Shared{" +
                "PAid='" + PAid + '\'' +
                ", photoID='" + photoID + '\'' +
                ", ownerID=" + ownerID +
                ", authID=" + authID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shared that = (Shared) o;
        return Objects.equals(PAid, that.PAid) && Objects.equals(photoID, that.photoID) && Objects.equals(ownerID, that.ownerID) && Objects.equals(authID, that.authID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(PAid, photoID, ownerID, authID);
    }

    public String getPAid() {
        return PAid;
    }

    public void setPAid(String PAid) {
        this.PAid = PAid;
    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public Integer getAuthID() {
        return authID;
    }

    public void setAuthID(Integer authID) {
        this.authID = authID;
    }

    public Shared(String PAid, String photoID, Integer ownerID, Integer authID) {
        this.PAid = PAid;
        this.photoID = photoID;
        this.ownerID = ownerID;
        this.authID = authID;
    }
}
