package com.bb.stardium.bench.service;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.domain.repository.RoomRepository;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.service.PlayerService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
class RoomServiceTest {

    public static final String PLAYER_EMAIL = "email2";
    public static final long ROOM_ID = 1L;
    @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    @MockBean
    private PlayerService playerService;

    private Address address;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Room room;
    private Room room2;
    private Player master;
    private Player player;

    @BeforeEach
    void setUp() {
        master = new Player("master", "email", "password");
        player = new Player("player", PLAYER_EMAIL, "password");
        address = new Address("서울시", "송파구", "루터회관 앞");
        startTime = LocalDateTime.of(2020, 11, 30, 10, 0);
        endTime = LocalDateTime.of(2020, 11, 30, 13, 0);
        room = new Room(ROOM_ID, "title", "intro", address, startTime, endTime, 10, master, new ArrayList<>());
        room2 = new Room(2L, "title2", "intro2", address, startTime, endTime, 12, master, new ArrayList<>());
    }

    @DisplayName("create method 성공")
    @Test
    public void createRoom() throws Exception {
        RoomRequestDto roomRequest =
                new RoomRequestDto("title", "intro", address, startTime, endTime, 10);
        given(roomRepository.save(any())).willReturn(room);

        roomService.create(roomRequest);

        verify(roomRepository).save(any());
    }

    @DisplayName("update method 성공")
    @Test
    public void updateRoom() throws Exception {
        given(roomRepository.findById(any())).willReturn(Optional.of(room));

        RoomRequestDto updateRequest = new RoomRequestDto("updatedTitle",
                "updatedIntro", address, startTime, endTime, 5);
        Long roomNumber = roomService.update(room.getId(), updateRequest);

        Room updatedRoom = roomRepository.findById(roomNumber).orElseThrow();
        assertThat(updatedRoom.getId()).isEqualTo(roomNumber);
        assertThat(updatedRoom.getTitle()).isEqualTo(updateRequest.getTitle());
        assertThat(updatedRoom.getIntro()).isEqualTo(updateRequest.getIntro());
        assertThat(updatedRoom.getPlayersLimit()).isEqualTo(updateRequest.getPlayersLimit());
    }

    @DisplayName("delete method 성공")
    @Test
    public void deleteRoom() throws Exception {
        given(roomRepository.findById(any())).willReturn(Optional.ofNullable(room));

        roomService.delete(room.getId());

        verify(roomRepository).delete(any());
    }

    @DisplayName("find method 성공")
    @Test
    public void findRoom() throws Exception {
        given(roomRepository.findById(any())).willReturn(Optional.ofNullable(room));

        roomService.findRoom(room.getId());

        verify(roomRepository).findById(any());
    }

    @DisplayName("findAllRoom method 성공")
    @Test
    public void findAllRoom() throws Exception {
        // given
        given(roomRepository.findAll()).willReturn(Lists.newArrayList(room, room2));

        // when
        roomService.findAllRooms();

        // then
        verify(roomRepository).findAll();
    }

    @Test
    void join() {
        given(playerService.findByPlayerEmail(any())).willReturn(player);
        given(roomRepository.findById(1L)).willReturn(Optional.of(room));

        roomService.join(PLAYER_EMAIL, room.getId());
    }
}