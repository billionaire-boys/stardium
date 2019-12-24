package com.bb.stardium.bench.service;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.domain.Room2;
import com.bb.stardium.bench.domain.repository.Room2Repository;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith({SpringExtension.class})
class RoomServiceTest {

    private static final String PLAYER_EMAIL = "email@email.com";

    @InjectMocks
    private RoomService roomService;

    @Mock
    private Room2Repository roomRepository;

    @Mock
    private PlayerService playerService;

    private Address address;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Room2 room1;
    private Room2 room2;
    private Room2 readyRoom;
    private Player master;
    private Player player;

    @BeforeEach
    void setUp() {
        master = Player.builder()
                .id(1L)
                .nickname("master")
                .email("master@email.com")
                .password("password")
                .build();

        player = Player.builder()
                .id(2L)
                .nickname("player")
                .email(PLAYER_EMAIL)
                .password("password")
                .build();

        address = Address.builder()
                .city("서울시").section("송파구").detail("루터회관 앞")
                .build();
        startTime = LocalDateTime.now().plusDays(1);
        endTime = LocalDateTime.now().plusDays(1).plusHours(3);

        room1 = new MockRoom(Room2.builder()
                .title("타이틀1")
                .intro("인트로1")
                .playersLimit(10)
                .address(address)
                .master(master)
                .startTime(startTime)
                .endTime(endTime)
                .build());

        room2 = new MockRoom(Room2.builder()
                .title("타이틀2")
                .intro("인트로2")
                .playersLimit(12)
                .address(address)
                .master(master)
                .startTime(startTime)
                .endTime(endTime)
                .build());

        readyRoom = Room2.builder()
                .title("title4")
                .intro("intro")
                .address(address)
                .startTime(startTime.plusHours(5))
                .endTime(endTime.plusHours(5))
                .playersLimit(2)
                .master(master)
                .build();

        readyRoom.addPlayer(master);
        readyRoom.addPlayer(player);
    }

    @Test
    void 생성() {
        RoomRequestDto requestDto = new RoomRequestDto("타이틀1", "인트로", address, startTime, endTime, 10, master);

        given(roomRepository.save(any(Room2.class))).willReturn(room1);

        long id = roomService.create(requestDto, master);

        assertThat(id).isEqualTo(10);
        verify(roomRepository).save(any(Room2.class));
    }

    @DisplayName("update method 성공")
    @Test
    void updateRoom() {
        RoomRequestDto updateRequest = new RoomRequestDto("updatedTitle",
                "updatedIntro", address, startTime, endTime, 5, master);

        // given
        given(roomRepository.findById(any())).willReturn(Optional.of(room1));

        //when
        long roomNumber = roomService.update(room1.getId(), updateRequest, master);

        assertThat(roomNumber).isEqualTo(5L);
        verify(roomRepository).findById(anyLong());
    }

    @DisplayName("방장이 방 삭제 성공")
    @Test
    void master_can_delete_room() {
        given(roomRepository.findById(anyLong())).willReturn(Optional.ofNullable(room1));
        given(playerService.findByPlayerEmail(any(String.class))).willReturn(master);

        roomService.delete(room1.getId(), room1.getMaster());

        verify(roomRepository).delete(room1);
    }

    @DisplayName("방장이 아닌 플레이어의 방 삭제 실패")
    @Test
    void player_cannot_delete_room() {
        given(roomRepository.findById(anyLong())).willReturn(Optional.ofNullable(room1));
        given(playerService.findByPlayerEmail(anyString())).willReturn(player);

        assertThatThrownBy(() ->
                roomService.delete(room1.getId(), player)).isInstanceOf(MasterAndRoomNotMatchedException.class);
    }

    @DisplayName("find method 성공")
    @Test
    void findRoom() {
        given(roomRepository.findById(anyLong())).willReturn(Optional.ofNullable(room1));

        Room2 room = roomService.findRoom(room1.getId());

        assertThat(room.getTitle()).isEqualTo(room1.getTitle());
        assertThat(room.getId()).isEqualTo(room1.getId());

        verify(roomRepository).findById(anyLong());
    }

    @DisplayName("findAllRoom method 성공")
    @Test
    void findAllRoom() {
        ArrayList<Room2> allRooms = Lists.newArrayList(room1, room2);
        given(roomRepository.findAll()).willReturn(allRooms);

        List<RoomResponseDto> resultRooms = roomService.findAllRooms();

        assertThat(resultRooms.size()).isEqualTo(allRooms.size());
        assertThat(resultRooms.contains(new RoomResponseDto(allRooms.get(0))));
        assertThat(resultRooms.contains(new RoomResponseDto(allRooms.get(1))));
        verify(roomRepository).findAll();
    }

    @Test
    void join() {
        given(playerService.findByPlayerEmail(anyString())).willReturn(player);
        given(roomRepository.findById(anyLong())).willReturn(Optional.of(room1));

        roomService.join(player, room1.getId());

        assertThat(room1.getPlayers().size()).isEqualTo(1);
        assertThat(room1.getPlayers().contains(player)).isTrue();
    }

    @Test
    void quit() {
        given(playerService.findByPlayerEmail(anyString())).willReturn(player);
        given(roomRepository.findById(anyLong())).willReturn(Optional.of(room1));

        roomService.quit(room1.getId(), player);

        assertThat(room1.getPlayers().contains(player)).isFalse();
    }

    @Test
    @DisplayName("레디인 방에서 나가기")
    void quit_in_ready_room() {
        given(playerService.findByPlayerEmail(anyString())).willReturn(player);
        given(roomRepository.findById(anyLong())).willReturn(Optional.of(readyRoom));

        assertThatThrownBy(() -> roomService.quit(999L, player)).isInstanceOf(FixedReadyRoomException.class);
    }

    @Test
    @DisplayName("레디인 방에서 방장이 방을 폭파 시도하나 실패")
    void delete_ready_room() {
        given(playerService.findByPlayerEmail(anyString())).willReturn(master);
        given(roomRepository.findById(anyLong())).willReturn(Optional.of(readyRoom));

        assertThatThrownBy(() -> roomService.delete(999L, master)).isInstanceOf(FixedReadyRoomException.class);
    }


    @DisplayName("자신이 참가한 방을 찾기")
    @Test
    void findPlayerJoinedRoom() {
        List<Room2> rooms = Lists.newArrayList(room1, room2);
        given(roomRepository.findByPlayersOrderByStartTimeAsc(any(Player.class))).willReturn(rooms);

        List<RoomResponseDto> actual = roomService.findPlayerJoinedRoom(master);
        List<RoomResponseDto> expected = rooms.stream()
                .map(RoomResponseDto::new).collect(Collectors.toList());

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("현재 시간 이후이고 참가가능 인원이 남아 있는 방을 찾기")
    @Test
    void findAllUnexpiredRooms() {
        List<Room2> rooms = Lists.newArrayList(room1, room2, readyRoom);
        given(roomRepository.findAll()).willReturn(rooms);

        List<RoomResponseDto> actual = roomService.findAllUnexpiredRooms();
        List<RoomResponseDto> expected = rooms.stream()
                .filter(Room2::hasRemainingSeat)
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("지역에 따라 필터링 된 방 찾기")
    @Test
    public void findRoomsFilterBySection() throws Exception {
        String section = "송파구";
        List<Room2> rooms = Lists.newArrayList(room1, room2);

        // given
        given(roomRepository.findAllByAddressSection(eq(section), any(Sort.class)))
                .willReturn(rooms);

        // when
        List<RoomResponseDto> reuslts = roomService.findRoomsFilterBySection(section);

        // then
        verify(roomRepository).findAllByAddressSection(eq(section), any(Sort.class));
    }

}

class MockRoom extends Room2 {
    MockRoom(Room2 room2) {
        super(room2.getTitle(), room2.getIntro(), room2.getPlayersLimit(), room2.getAddress(),
                room2.getStartTime(), room2.getEndTime(), room2.getMaster());
    }

    @Override
    public Long getId() {
        return (long) getPlayersLimit();
    }
}