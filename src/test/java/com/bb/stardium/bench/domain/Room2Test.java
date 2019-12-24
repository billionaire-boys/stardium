package com.bb.stardium.bench.domain;

import com.bb.stardium.player.domain.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

@DataJpaTest
class Room2Test {
    @Autowired
    private TestEntityManager tm;

    @Test
    void name() {
        Player player = Player.builder()
                .nickname("노수진")
                .password("비밀번호")
                .email("soojinroh@naver.com")
                .build();

        Room2 room2 = Room2.builder()
                .title("타이틀")
                .intro("인트로")
                .playersLimit(3)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .master(player)
                .build();

        tm.persist(room2);
    }
}