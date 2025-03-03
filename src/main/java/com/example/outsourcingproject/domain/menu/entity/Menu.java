package com.example.outsourcingproject.domain.menu.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Table(name = "menus")
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private int price;

    @ColumnDefault("false")
    private boolean deleteFlag;

    public Menu(String menuName, int price) {
        this.menuName = menuName;
        this.price = price;
    }

    // 메뉴 menuName만 수정
    public void updateMenuName(String menuName) {
        this.menuName = menuName;
    }

    // 메뉴 price만 수정
    public void updatePrice(int price) {
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
