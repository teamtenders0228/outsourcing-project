package com.example.outsourcingproject.domain.menu.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Table(name = "menus")
@NoArgsConstructor
@AllArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer price;

    @ColumnDefault("false")
    private boolean deleteFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Menu(String menuName, Integer price, Store store) {
        this.menuName = menuName;
        this.price = price;
        this.store = store;
    }

    // 메뉴 menuName만 수정
    public void updateMenuName(String menuName) {
        this.menuName = menuName;
    }

    // 메뉴 price만 수정
    public void updatePrice(Integer price) {
        this.price = price;
    }

    // 가격에 (,) 넣기  ex) 20,000
    public String priceToString() {
        // String -> int
        return String.format("%,d", price);
    }

    // 메뉴 삭제시 deleteFlag -> true로 변환
    public void updateDeleteFlag() {
        this.deleteFlag = true;
    }
}
