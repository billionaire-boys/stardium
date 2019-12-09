package com.bb.stardium.player.domain.repository;

import com.bb.stardium.player.domain.Player2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class Player2RepositoryTest {

    @Autowired
    private Player2Repository repository;

    private Player2 player;

    @BeforeEach
    void setUp() {
        player = Player2.builder()
                .email("a@a.com")
                .nickname("andole")
                .password("abcd")
                .build();
    }

    @Test
    @DisplayName("이메일로 플레이어를 찾아내는지")
    void findByEmail() {
        repository.save(player);

        Optional<Player2> persist = repository.findByEmail("a@a.com");

        assertThat(persist.isPresent()).isTrue();
        assertThat(persist.get().getNickname()).isEqualTo("andole");
        assertThat(persist.get().getEmail()).isEqualTo("a@a.com");
    }
}