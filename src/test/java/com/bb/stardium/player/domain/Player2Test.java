package com.bb.stardium.player.domain;

import com.bb.stardium.mediafile.MediaFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class Player2Test {
    @Autowired
    TestEntityManager em;

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
    @DisplayName("저장시 생성시간이 등록되는지")
    void createTime() {
        Player2 persist = em.persist(player);

        assertThat(persist.getCreateDateTime()).isNotNull();
    }

    @Test
    @DisplayName("업데이트시 시간이 기록되는지")
    void updateTime() {
        Player2 persist = em.persist(player);

        OffsetDateTime createDateTime = persist.getCreateDateTime();
        OffsetDateTime updateDateTime = persist.getUpdateDateTime();

        player.updateStatusMessage("Hello");
        em.flush();

        assertThat(persist.getUpdateDateTime()).isNotEqualTo(updateDateTime);
        assertThat(persist.getCreateDateTime()).isEqualTo(createDateTime);
        assertThat(persist.getStatusMessage()).isEqualTo("Hello");
    }

    @Test
    @DisplayName("프로필 사진과 함께 저장되는지")
    void profile() {
        Player2 player = Player2.builder()
                .email("a@a.com")
                .nickname("andole")
                .password("abcd")
                .profile(new MediaFile(""))
                .build();

        Player2 persist = em.persist(player);

        assertThat(persist.getProfile()).isNotNull();
    }

    @Test
    @DisplayName("패스워드를 제대로 검사하는지")
    void matchPassword() {
        assertThat(player.isMatchPassword("wrong")).isFalse();
        assertThat(player.isMatchPassword("abcd")).isTrue();
    }

    @Test
    @DisplayName("업데이트가 잘 동작하는지")
    void update() {
        em.persist(player);

        Player2 update = Player2.builder()
                .email("update@update.com")
                .nickname("update")
                .password("password")
                .build();

        player.update(update);
        em.persist(player);

        assertThat(player.getEmail()).isEqualTo("update@update.com");
        assertThat(player.getNickname()).isEqualTo("update");
    }
}