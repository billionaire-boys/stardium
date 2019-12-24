package com.bb.stardium.bench.domain.repository;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.player.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
class RoomRepositoryTest {

    @Autowired
    Room2Repository roomRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        roomRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자 이메일로 참가한 Room을 찾기")
    void findByPlayers_Email() {
        Player player1 = Player.builder()
                .nickname("nick1")
                .email("email1@email.com")
                .password("password")
                .build();

        Player player2 = Player.builder()
                .nickname("nick2")
                .email("email2@email.com")
                .password("password")
                .build();

        testEntityManager.persist(player1);
        testEntityManager.persist(player2);

        Address address = Address.builder()
                .city("서울시").section("송파구")
                .detail("루터회관 앞")
                .build();
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1).plusHours(2);
        Room room1 = Room.builder()
                .title("title1")
                .intro("intro")
                .address(address)
                .startTime(startTime)
                .endTime(endTime)
                .playersLimit(10)
                .master(player1)
                .build();

        Room room = Room.builder()
                .title("title2")
                .intro("intro")
                .address(address)
                .startTime(startTime.plusHours(3))
                .endTime(endTime.plusHours(3))
                .playersLimit(10)
                .master(player2)
                .build();

        room1.addPlayer(player1);
        room.addPlayer(player2);

        room1 = roomRepository.save(room1);
        room = roomRepository.save(room);

        List<Room> rooms = roomRepository.findByPlayersOrderByStartTimeAsc(player1);
        assertThat(rooms).contains(room1);
        assertThat(rooms).doesNotContain(room);
    }
}
