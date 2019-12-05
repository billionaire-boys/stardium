package com.bb.stardium.common.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    @GetMapping("/")
    public String homepage() {
        return "main_all_room.html";
    }

    @GetMapping("/myRoom")
    public String myRoomPage() {
        return "main_my_room.html";
    }
}
