package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.service.UserService;
// import com.example.demo.model.User;

@Controller
public class AuthController {
    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String show(Model model) {
        service.SignUp("DuyThanh", "123456");
        model.addAttribute("user", service.getUserById(2l));
        return "index";
    }
}
