package com.myprojects.walletdemoproject.project.controller;

import com.myprojects.walletdemoproject.project.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи",
        description = "Контроллер для работы с пользователями кошелька")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }


}
