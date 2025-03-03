package com.example.outsourcingproject.menu.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "menu")
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    private String name;

    private Integer price;

    private Boolean deleteFlag;

    public Menu (Store store, String name, Integer price, Boolean deleteFlag){
        this.store = store;
        this.name = name;
        this.price = price;
        this.deleteFlag = deleteFlag;
    }
}
