package com.example.outsourcingproject.domain.menu.controller;

import com.example.outsourcingproject.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
}
