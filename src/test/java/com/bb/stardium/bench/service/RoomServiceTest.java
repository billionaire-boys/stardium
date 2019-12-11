package com.bb.stardium.bench.service;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.domain.repository.RoomRepository;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.dto.RoomResponseDto;
import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.service.PlayerService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = RoomService.class)
class RoomServiceTest {

    private static final String PLAYER_EMAIL = "email@email.com";
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
    private Room room3;
    private Room room4;
    private Player master;
    private Player player;

    @BeforeEach
    void setUp() {
        master = new Player("master", "email", "password");
        player = new Player("player", PLAYER_EMAIL, "password");
        address = Address.builder()
                .city("서울시").section("송파구").detail("루터회관 앞")
                .build();
        startTime = LocalDateTime.of(2020, 11, 30, 10, 0);
        endTime = LocalDateTime.of(2020, 11, 30, 13, 0);

        room = new Room(1L, "title", "intro", address, startTime, endTime, 10, master, new ArrayList<>(List.of(master)));
        room2 = new Room(2L, "title2", "intro2", address, startTime, endTime, 12, master, new ArrayList<>(List.of(player)));
        room3 = Room.builder().id(3L).title("title3").intro("intro").address(address)
                .startTime(startTime.minusDays(4)).endTime(endTime.minusDays(4))
                .playersLimit(10).master(master)
                .players(new ArrayList<>(Arrays.asList(master, player))).build();
        room4 = Room.builder().id(4L).title("title4").intro("intro").address(address)
                .startTime(startTime.plusHours(5)).endTime(endTime.plusHours(5))
                .playersLimit(2).master(player)
                .players(new ArrayList<>(Arrays.asList(master, player))).build();

    }

    @DisplayName("create method 성공")
    @Test
    public void createRoom() throws Exception {
        RoomRequestDto roomRequest =
                new RoomRequestDto("title", "intro", address, startTime, endTime, 10, master);
        given(roomRepository.save(any())).willReturn(room);

        roomService.create(roomRequest, master);

        verify(roomRepository).save(any());
    }

    @DisplayName("update method 성공")
    @Test
    public void updateRoom() throws Exception {
        given(roomRepository.findById(any())).willReturn(Optional.of(room));

        RoomRequestDto updateRequest = new RoomRequestDto("updatedTitle",
                "updatedIntro", address, startTime, endTime, 5, master);
        Long roomNumber = roomService.update(room.getId(), updateRequest, master);

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
        given(roomRepository.findAll()).willReturn(Lists.newArrayList(room, room2));

        roomService.findAllRooms();

        verify(roomRepository).findAll();
    }

    @Test
    void join() {
        given(playerService.findByPlayerEmail(any())).willReturn(player);
        given(roomRepository.findById(1L)).willReturn(Optional.of(room));

        roomService.join(PLAYER_EMAIL, room.getId());

        assertThat(room.getPlayers().contains(player)).isTrue();
        assertThat(player.getRooms().contains(room)).isTrue();
    }

    @Test
    void quit() {
        given(playerService.findByPlayerEmail(any())).willReturn(player);
        given(roomRepository.findById(1L)).willReturn(Optional.of(room));

        roomService.quit(PLAYER_EMAIL, room.getId());
        assertThat(room.getPlayers().contains(player)).isFalse();
        assertThat(player.getRooms().contains(room)).isFalse();

    }

    @DisplayName("자신이 참가한 방을 찾기")
    @Test
    void findPlayerJoinedRoom() {
        given(roomRepository.findByPlayers_Email(anyString())).willReturn(List.of(room, room2, room4));

        List<RoomResponseDto> actual = roomService.findPlayerJoinedRoom(master);
        List<RoomResponseDto> expected = List.of(room, room2, room4).stream()
                .map(this::toResponseDto).collect(Collectors.toList());

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 시간 이후이고 참가가능 인원이 남아 있는 방을 찾기")
    @Test
    @Disabled
    void findAllUnexpiredRooms() {
        given(roomRepository.findAll()).willReturn(List.of(room, room2, room3, room4));

        List<RoomResponseDto> actual = roomService.findAllUnexpiredRooms();
        List<RoomResponseDto> expected = List.of(room3, room, room2).stream()
                .map(this::toResponseDto).collect(Collectors.toList());

        assertThat(actual).isEqualTo(expected);
    }

    private RoomResponseDto toResponseDto(Room room) {
        return RoomResponseDto.builder()
                .title(room.getTitle())
                .intro(room.getIntro())
                .address(String.format("%s %s %s",
                        room.getAddress().getCity(),
                        room.getAddress().getSection(),
                        room.getAddress().getDetail()))
                .playTime(String.format("%s - %s",
                        room.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        room.getEndTime().format(DateTimeFormatter.ofPattern("dd"))))
                .playLimits(room.getPlayersLimit())
                .master(room.getMaster())
                .playerCount(room.getPlayers().size())
                .id(room.getId())
                .build();
    }

}