package com.bb.stardium.bench.service;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.domain.repository.RoomRepository;
import com.bb.stardium.bench.dto.Address;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    private Address address;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Room room;

//    @BeforeEach
//    void setUp() {
//        address = new Address("서울시", "송파구", "루터회관 앞");
//        startTime = LocalDateTime.of(2020, 11, 30, 10, 0);
//        endTime = LocalDateTime.of(2020, 11, 30, 13, 0);
//        room = new Room(1L, "title", "intro", address, startTime, endTime, 10);
//    }

//    @DisplayName("create method 성공")
//    @Test
//    public void createRoom() throws Exception {
//        // given
//        RoomRequestDto roomRequest = new RoomRequestDto("title", "intro", address, startTime, endTime, 10);
//        given(roomRepository.save(any())).willReturn(room);
//
//        // when
//        roomService.create(roomRequest);
//
//        // then
//        verify(roomRepository).save(any());
//    }

//    @DisplayName("update method 성공")
//    @Test
//    public void updateRoom() throws Exception {
//        // given
//        given(roomRepository.findById(any())).willReturn(Optional.of(room));
//
//        // when
//        RoomRequestDto updateRequest = new RoomRequestDto("updatedTitle", "updatedIntro", address, startTime, endTime, 5);
//        Long roomNumber = roomService.update(room.getId(), updateRequest);
//
//        // then
//        Room updatedRoom = roomRepository.findById(roomNumber).orElseThrow();
//        assertThat(updatedRoom.getId()).isEqualTo(roomNumber);
//        assertThat(updatedRoom.getTitle()).isEqualTo(updateRequest.getTitle());
//        assertThat(updatedRoom.getIntro()).isEqualTo(updateRequest.getIntro());
//        assertThat(updatedRoom.getPlayersLimit()).isEqualTo(updateRequest.getPlayersLimit());
//    }

//    @DisplayName("delete method 성공")
//    @Test
//    public void deleteRoom() throws Exception {
//        // given
//        given(roomRepository.findById(any())).willReturn(Optional.ofNullable(room));
//
//        // when
//        roomService.delete(room.getId());
//
//        // then
//        verify(roomRepository).delete(any());
//    }

//    @DisplayName("find method 성공")
//    @Test
//    public void findRoom() throws Exception {
//        // given
//        given(roomRepository.findById(any())).willReturn(Optional.ofNullable(room));
//
//        // when
//        roomService.findRoom(room.getId());
//
//        // then
//        verify(roomRepository).findById(any());
//    }

}