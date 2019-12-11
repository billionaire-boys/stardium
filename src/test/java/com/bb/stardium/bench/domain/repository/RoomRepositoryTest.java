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
    RoomRepository roomRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        roomRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자 이메일로 참가한 Room을 찾기")
    void findByPlayers_Email() {
        Player player1 = new Player("nick1", "email1", "password");
        Player player2 = new Player("nick2", "email2", "password");
        Player player3 = new Player("nick3", "email3", "password");
        testEntityManager.persist(player1);
        testEntityManager.persist(player2);
        testEntityManager.persist(player3);

        Address address = Address.builder()
                    .city("서울시").section("송파구")
                    .detail("루터회관 앞")
                    .build();
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1).plusHours(2);
        Room room1 = Room.builder().id(100L).title("title1").intro("intro").address(address)
                .startTime(startTime).endTime(endTime)
                .playersLimit(10).master(player1)
                .players(List.of(player1)).build();
        Room room2 = Room.builder().id(200L).title("title2").intro("intro").address(address)
                .startTime(startTime.plusHours(3)).endTime(endTime.plusHours(3))
                .playersLimit(10).master(player2)
                .players(List.of(player2, player3)).build();
        Room room3 = Room.builder().id(300L).title("title3").intro("intro").address(address)
                .startTime(startTime.plusDays(4)).endTime(endTime.plusDays(4))
                .playersLimit(10).master(player3)
                .players(List.of()).build();
        Room room4 = Room.builder().id(400L).title("title4").intro("intro").address(address)
                .startTime(startTime.plusHours(5)).endTime(endTime.plusHours(5))
                .playersLimit(2).master(player2)
                .players(List.of(player1, player2, player3)).build();
        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);
        roomRepository.save(room4);

        List<Room> rooms = roomRepository.findByPlayers_Email(player1.getEmail());
        for (Room room : rooms) {
            assertThat(room.getPlayers().contains(player1)).isTrue();
        }
    }
}