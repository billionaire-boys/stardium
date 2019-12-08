package com.bb.stardium.common.web.controller;

import com.bb.stardium.bench.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = MainPageController.class)
class MainPageControllerTest {

    @MockBean
    private RoomService roomService;

    @Autowired
    private MockMvc mockMvc;

    @Test
   @DisplayName("메인 페이지 접속")
    void homepage() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login.html"));
    }

    @Test
    @DisplayName("마이룸 페이지 접속")
    void myRoomPage() throws Exception {
        mockMvc.perform(get("/myRoom"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("main_my_room.html"));
    }
}