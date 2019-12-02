package com.bb.stardium;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String home() {
        return "login.html";
    }

    @GetMapping("/user/new")
    public String signup() {
        return "signup.html";
    }

    @GetMapping("/user/edit")
    public String edit() {
        return "user-edit.html";
    }
}
