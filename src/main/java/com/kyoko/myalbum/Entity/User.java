package com.kyoko.myalbum.Entity;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * @author young
 * @create 2023/3/11 23:05
 * @Description
 */
@Entity
public class User {
    @Id
    @SequenceGenerator(//提供序列生成主键的支持，后
            name = "user_id_sequence",
            sequenceName = "user_id_sequence",
            allocationSize = 1,
            initialValue = 10_000
    )
    //支持根据数据库序列号（Sequence）技术生成实体标识，默认步长50
    //mysql不支持nextval（）
    @GeneratedValue(//使用序列生成主键，先
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence"
    )
    private Integer uid;
    private String username;
    private String password;
    private String nickname;

    public User(Integer uid,String username, String password, String nickname) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public User() {

    }


    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(uid, user.uid) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(nickname, user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, username, password, nickname);
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
