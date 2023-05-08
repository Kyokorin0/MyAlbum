package com.kyoko.myalbum.entity;

import com.kyoko.myalbum.enumCode.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


/**
 * @author young
 * @create 2023/3/11 23:05
 * @Description DO Not Use User
 * 实现security的UserDetails
 * 也可以extends from User which implemented UserDetails
 */
@Entity//支持jpa自动建表
//一下注解都是自动生成对应方法，等效@Data
//Using @Data for JPA entities is not recommended. It can cause severe performance and memory consumption issues.
@Data
//以下生成全参、无参构造函数，支持@Entity和@Builder
@AllArgsConstructor
@NoArgsConstructor
@Builder//支持链式构造对象实例,需要显式定义无参构造函数和全参构造函数
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class MyUser implements UserDetails {
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
    //username、password为security需要的字段，getPassword()由@Data自动生成了
    private String password;
    private String nickname;
    private boolean isAccountNonExpired=true;
    private boolean isAccountNonLocked=true;
    private boolean isCredentialsNonExpired=true;
    private boolean isEnabled=false;


    @Column(name = "email")
    private String email;
    //以下为security需要而添加的字段
    @Enumerated(value = EnumType.STRING)
    //ORDINAL,按照枚举的下标，使用Enum类型实例在Enum中声明的顺序,通过这个序号来将Enum类型字段映射成int类型来存储；
    //STRING,按照枚举的名字，使用Enum类型实例中的name属性来完成映射，将Enum类型映射成字符串的方式
    private Role role;


    @Override
    //为用户授权角色，需要添加role字段,返回SimpleGrantedAuthority列表
    //这里是一个角色，可以一个用户对应多个角色，即多个SimpleGranted...对象生成的列表
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    //登陆需要的用户名，这里采用email？
    @Override
    public String getUsername() {
        return email;
    }

    //检查账户没有过期，ture
    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    //检查账户没有被锁定，true
    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    //检查认证是否过期
    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    //是否启用，false
    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
