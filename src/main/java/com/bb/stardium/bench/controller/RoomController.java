package com.bb.stardium.bench.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    @GetMapping("/createForm")
    public String createFrom() {
        return "createRoom";
    }
}
