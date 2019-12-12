package com.bb.stardium.chat.repository;

import com.bb.stardium.chat.domain.ChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChatMessageRepositoryTest {

    private static final String CONTENTS = "contents";
    private static final int PLAYER_ID = 1;
    private static final int ROOM_ID = 1;
    private static final String NICKNAME = "nickname";

    @Autowired
    TestEntityManager testEntityManager;
    ChatMessage first = ChatMessage.builder()
            .contents(CONTENTS)
            .playerId(PLAYER_ID)
            .roomId(ROOM_ID)
            .playerNickname(NICKNAME)
            .timestamp(OffsetDateTime.now())
            .build();
    ChatMessage middle = ChatMessage.builder()
            .contents(CONTENTS)
            .playerId(PLAYER_ID)
            .playerNickname(NICKNAME)
            .roomId(ROOM_ID)
            .timestamp(OffsetDateTime.now().plusDays(1))
            .build();
    ChatMessage end = ChatMessage.builder()
            .contents(CONTENTS)
            .playerId(PLAYER_ID)
            .roomId(ROOM_ID)
            .playerNickname(NICKNAME)
            .timestamp(OffsetDateTime.now().plusDays(2))
            .build();
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @BeforeEach
    void setup() {
        chatMessageRepository.deleteAll();
    }

    @Test
    @DisplayName("메시지 시간순 정렬")
    public void readByTimestamp() {
        testEntityManager.persist(end);
        testEntityManager.persist(first);
        testEntityManager.persist(middle);
        testEntityManager.flush();

        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomIdOrderByTimestamp(ROOM_ID);

        assertThat(chatMessages).isEqualTo(List.of(first, middle, end));
    }
}