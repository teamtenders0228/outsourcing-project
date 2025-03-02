package com.example.outsourcingproject.domain.menu.repository;

import com.example.outsourcingproject.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository <Menu, Long> {
}
