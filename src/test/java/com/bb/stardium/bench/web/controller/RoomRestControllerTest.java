package com.bb.stardium.bench.web.controller;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.service.RoomService;
import com.bb.stardium.bench.web.restcontroller.RoomRestController;
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
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoomRestController.class)
@Import(MediaFileResourceLocation.class)
public class RoomRestControllerTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Player player =
            Player.builder()
            .nickname("nickname")
            .password("password")
            .email("email@email.com")
            .rooms(new ArrayList<>())
            .build();
    private final Room mockRoom = mock(Room.class);
    private final Address mockAddress = mock(Address.class);
    private final RoomRequestDto requestDto = new RoomRequestDto(
            "title",
            "intro",
            Address.builder()
                    .city("서울시").section("송파구")
                    .detail("루터회관").build(),
            LocalDateTime.now().plusDays(1).plusHours(2),
            LocalDateTime.now().plusDays(1).plusHours(3),
            10,
            player
    );

    @MockBean
    private PlayerService playerService;

    @MockBean
    private RoomService roomService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        given(mockRoom.getId()).willReturn(1L);
        given(mockRoom.getAddress()).willReturn(mockAddress);
        given(roomService.findRoom(anyLong())).willReturn(mockRoom);
        given(playerService.findByPlayerEmail(anyString())).willReturn(player);
    }

    private static String asJsonString(final Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException ignored) {
            return "{}";
        }
    }

    @Test
    @DisplayName("방 생성하기")
    void postCreate() throws Exception {
        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestDto))
                .sessionAttr("login", new PlayerResponseDto(player)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("방에 참여하기")
    void postJoin() throws Exception {
        mockMvc.perform(post("/rooms/join/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("login", new PlayerResponseDto(player)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("방에서 나가기")
    void postQuit() throws Exception {
        mockMvc.perform(post("/rooms/quit/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("login", new PlayerResponseDto(player)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("방 편집하기")
    void putUpdate() throws Exception {
        mockMvc.perform(put("/rooms/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(asJsonString(requestDto))
                .sessionAttr("login", new PlayerResponseDto(player)))
                .andDo(print())
                .andExpect(status().isOk());
        verify(roomService).update(anyLong(), any(), any());
    }

}
