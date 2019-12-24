package com.bb.stardium.bench.service;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.domain.repository.RoomRepository;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.dto.RoomResponseDto;
import com.bb.stardium.bench.service.exception.FixedReadyRoomException;
import com.bb.stardium.bench.service.exception.MasterAndRoomNotMatchedException;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private Room room1;
    private Room room2;
    private Room room3;
    private Room room4;
    private Player master;
    private Player player;

    @BeforeEach
    void setUp() {
        master = Player.builder()
                .id(1L)
                .nickname("master")
                .email("master@email.com")
                .password("password")
                .rooms(new ArrayList<>())
                .build();

        player = Player.builder()
                .id(2L)
                .nickname("player")
                .email(PLAYER_EMAIL)
                .password("password")
                .rooms(new ArrayList<>())
                .build();

        address = Address.builder()
                .city("서울시").section("송파구").detail("루터회관 앞")
                .build();
        startTime = LocalDateTime.now().plusDays(1);
        endTime = LocalDateTime.now().plusDays(1).plusHours(3);

        room1 = new Room(1L, "title", "intro", 10, address, startTime, endTime, master, new ArrayList<>(List.of(master)));
        room2 = new Room(2L, "title2", "intro2", 12, address, startTime, endTime, master, new ArrayList<>(List.of(player)));
        room3 = Room.builder().id(3L).title("title3").intro("intro").address(address)
                .startTime(startTime.minusDays(4)).endTime(endTime.minusDays(4))
                .playersLimit(10).master(master)
                .players(new ArrayList<>(Arrays.asList(master, player))).build();
        room4 = Room.builder().id(4L).title("title4").intro("intro").address(address)
                .startTime(startTime.plusHours(5)).endTime(endTime.plusHours(5))
                .playersLimit(2).master(master)
                .players(new ArrayList<>(Arrays.asList(master, player))).build();
    }

    @DisplayName("create method 성공")
    @Test
    void createRoom() {
        RoomRequestDto roomRequest =
                new RoomRequestDto("title", "intro", address, startTime, endTime, 10, master);
        given(roomRepository.save(any())).willReturn(room1);

        roomService.create(roomRequest, master);

        verify(roomRepository).save(any());
    }

    @DisplayName("update method 성공")
    @Test
    void updateRoom() {
        given(roomRepository.findById(any())).willReturn(Optional.of(room1));

        RoomRequestDto updateRequest = new RoomRequestDto("updatedTitle",
                "updatedIntro", address, startTime, endTime, 5, master);
        Long roomNumber = roomService.update(room1.getId(), updateRequest, master);

        Room updatedRoom = roomRepository.findById(roomNumber).orElseThrow();
        assertThat(updatedRoom.getId()).isEqualTo(roomNumber);
        assertThat(updatedRoom.getTitle()).isEqualTo(updateRequest.getTitle());
        assertThat(updatedRoom.getIntro()).isEqualTo(updateRequest.getIntro());
        assertThat(updatedRoom.getPlayersLimit()).isEqualTo(updateRequest.getPlayersLimit());
    }

    @DisplayName("방장이 방 삭제 성공")
    @Test
    void master_can_delete_room() {
        given(roomRepository.findById(any())).willReturn(Optional.ofNullable(room1));
        given(playerService.findByPlayerEmail(room1.getMaster().getEmail())).willReturn(room1.getMaster());

        roomService.delete(room1.getId(), room1.getMaster());

        verify(roomRepository).delete(room1);
    }

    @DisplayName("방장이 아닌 플레이어의 방 삭제 실패")
    @Test
    void player_cannot_delete_room() {
        given(roomRepository.findById(any())).willReturn(Optional.ofNullable(room1));
        given(playerService.findByPlayerEmail(player.getEmail())).willReturn(player);

        assertThrows(MasterAndRoomNotMatchedException.class, () -> {
            roomService.delete(room1.getId(), player);
        });
    }

    @DisplayName("find method 성공")
    @Test
    void findRoom() {
        given(roomRepository.findById(any())).willReturn(Optional.ofNullable(room1));

        roomService.findRoom(room1.getId());

        verify(roomRepository).findById(any());
    }

    @DisplayName("findAllRoom method 성공")
    @Test
    void findAllRoom() {
        given(roomRepository.findAll()).willReturn(Lists.newArrayList(room1, room2));

        roomService.findAllRooms();

        verify(roomRepository).findAll();
    }

    @Test
    void join() {
        given(playerService.findByPlayerEmail(any())).willReturn(player);
        given(roomRepository.findById(1L)).willReturn(Optional.of(room1));

        roomService.join(player, room1.getId());

        assertThat(room1.getPlayers().contains(player)).isTrue();
        assertThat(player.getRooms().contains(room1)).isTrue();
    }

    @Test
    void quit() {
        given(playerService.findByPlayerEmail(any())).willReturn(player);
        given(roomRepository.findById(1L)).willReturn(Optional.of(room1));

        roomService.quit(player, room1.getId());
        assertThat(room1.getPlayers().contains(player)).isFalse();
        assertThat(player.getRooms().contains(room1)).isFalse();
    }

    @Test
    @DisplayName("레디인 방에서 나가기")
    void quit_in_ready_room() {
        Room readyRoom = room4;
        given(playerService.findByPlayerEmail(any())).willReturn(player);
        given(roomRepository.findById(readyRoom.getId())).willReturn(Optional.of(readyRoom));

        assertThrows(FixedReadyRoomException.class, () -> {
            roomService.quit(player, readyRoom.getId());
        });
    }

    @Test
    @DisplayName("레디인 방에서 방장이 방을 폭파 시도하나 실패")
    void delete_ready_room() {
        Room readyRoom = room4;
        given(playerService.findByPlayerEmail(any())).willReturn(master);
        given(roomRepository.findById(readyRoom.getId())).willReturn(Optional.of(readyRoom));

        assertThrows(FixedReadyRoomException.class, () -> {
            roomService.delete(readyRoom.getId(), master);
        });
    }


    @DisplayName("자신이 참가한 방을 찾기")
    @Test
    void findPlayerJoinedRoom() {
        given(roomRepository.findByPlayers_Email(anyString())).willReturn(List.of(room1, room2, room4));

        List<RoomResponseDto> actual = roomService.findPlayerJoinedRoom(master);
        List<RoomResponseDto> expected = List.of(room1, room2, room4).stream()
                .map(RoomResponseDto::new).collect(Collectors.toList());

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 시간 이후이고 참가가능 인원이 남아 있는 방을 찾기")
    @Test
    void findAllUnexpiredRooms() {
        given(roomRepository.findAll()).willReturn(List.of(room1, room2, room3, room4));

        List<RoomResponseDto> actual = roomService.findAllUnexpiredRooms();
        List<RoomResponseDto> expected = List.of(room1, room2).stream()
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("지역에 따라 필터링 된 방 찾기")
    @Test
    public void findRoomsFilterBySection() throws Exception {
        // given
        String section = "송파구";
        given(roomRepository.findAllByAddressSectionOrderByStartTimeAsc(section)).willReturn(List.of(room1, room2, room3, room4));

        // when
        roomService.findRoomsFilterBySection(section);

        // then
        verify(roomRepository).findAllByAddressSectionOrderByStartTimeAsc(section);
    }

}