package com.bb.stardium.bench.web.controller;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.service.RoomService;
import com.bb.stardium.mediafile.config.MediaFileResourceLocation;
import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = RoomController.class)
@Import(MediaFileResourceLocation.class)
class RoomControllerTest {

    private final Player mockPlayer = mock(Player.class);
    private final Room mockRoom = mock(Room.class);
    private final Address mockAddress = mock(Address.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        given(mockRoom.getId()).willReturn(1L);
        given(mockRoom.getAddress()).willReturn(mockAddress);
        given(roomService.findRoom(anyLong())).willReturn(mockRoom);
        given(playerService.findByPlayerEmail(anyString())).willReturn(mockPlayer);
    }

    @Test
    @DisplayName("방 목록 페이지 접속")
    void getMainRoomList() throws Exception {
        mockMvc.perform(get("/room"))
                .andExpect(status().isOk())
                .andExpect(view().name("main_my_room"));
    }

    @Test
    @DisplayName("방 생성 페이지 접속")
    void getCreateFrom() throws Exception {
        mockMvc.perform(get("/room/createForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_room"));
    }

    @Test
    @DisplayName("방 수정 페이지 접속")
    void updateForm() throws Exception {
        mockMvc.perform(get("/room/updateForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("update_room"));
    }
}