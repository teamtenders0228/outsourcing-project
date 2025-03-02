package com.example.outsourcingproject.user.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.user.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private String phone;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean deleteFlag;

    public User(String name, String email, String password, String phone, String address, Role role, boolean deleteFlag){
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.deleteFlag = deleteFlag;
    }

}
