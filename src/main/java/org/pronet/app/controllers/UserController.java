package org.pronet.app.controllers;

import org.pronet.app.payloads.BankResponse;
import org.pronet.app.payloads.UserRequest;
import org.pronet.app.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-account")
    public BankResponse createAccount(@RequestBody UserRequest request) {
        return userService.createAccount(request);
    }
}
