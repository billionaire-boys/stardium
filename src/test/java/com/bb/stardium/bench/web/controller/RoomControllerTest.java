package com.bb.stardium.bench.web.controller;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.service.RoomService;
import com.bb.stardium.mediafile.config.MediaFileResourceLocation;
import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.dto.PlayerResponseDto;
import com.bb.stardium.player.service.PlayerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = RoomController.class)
@Import(MediaFileResourceLocation.class)
class RoomControllerTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Player mockPlayer = mock(Player.class);
    private final Room mockRoom = mock(Room.class);
    private final Address mockAddress = mock(Address.class);
    private final RoomRequestDto requestDto = new RoomRequestDto(
            "title",
            "intro",
            new Address("서울시", "송파구", "루터회관"),
            LocalDateTime.now().plusDays(1).plusHours(2),
            LocalDateTime.now().plusDays(1).plusHours(3),
            10,
            mockPlayer
    );

    @MockBean
    private PlayerService playerService;

    @MockBean
    private RoomService roomService;

    @Autowired
    private MockMvc mockMvc;

    private static String asJsonString(final Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException ignored) {
            return "{}";
        }
    }

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
        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(view().name("main_my_room"));
    }

    @Test
    @DisplayName("방 생성 페이지 접속")
    void getCreateFrom() throws Exception {
        mockMvc.perform(get("/rooms/createForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_room"));
    }

    @Test
    @DisplayName("방 수정 페이지 접속")
    void updateForm() throws Exception {
        mockMvc.perform(get("/rooms/updateForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("updateRoom"));
    }

    @Test
    @DisplayName("방 생성하기")
    void postCreate() throws Exception {
        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestDto))
                .sessionAttr("login", new PlayerResponseDto(mockPlayer)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("방에 참여하기")
    void postJoin() throws Exception {
        mockMvc.perform(post("/rooms/join/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("login", new PlayerResponseDto(mockPlayer)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("방에서 나가기")
    void postQuit() throws Exception {
        mockMvc.perform(post("/rooms/quit/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("login", new PlayerResponseDto(mockPlayer)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("방 번호로 특정 방에 들어가기")
    void getRoomById() throws Exception {
        mockMvc.perform(get("/rooms/{id}", 1)
                .sessionAttr("login", new PlayerResponseDto(mockPlayer)))
                .andExpect(status().isOk());
        verify(mockRoom).getId();
    }

    @Test
    @DisplayName("방 편집하기")
    void putUpdate() throws Exception {
        mockMvc.perform(put("/rooms/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestDto))
                .sessionAttr("login", new PlayerResponseDto(mockPlayer)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(roomService).update(anyLong(), any(), any());
    }
}