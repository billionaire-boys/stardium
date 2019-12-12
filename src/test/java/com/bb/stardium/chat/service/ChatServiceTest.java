package com.bb.stardium.chat.service;

import com.bb.stardium.chat.domain.ChatMessage;
import com.bb.stardium.chat.dto.ChatMessageRequestDto;
import com.bb.stardium.chat.repository.ChatMessageRepository;
import com.bb.stardium.player.service.PlayerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = ChatService.class)
class ChatServiceTest {

    @MockBean
    PlayerService playerService;

    @MockBean
    ChatMessageRepository chatMessageRepository;

    @Autowired
    ChatService chatService;

    ChatMessage chatMessage = mock(ChatMessage.class);

    @Test
    @DisplayName("채팅 저장")
    void save() {
        ChatMessageRequestDto chatMessageRequestDto =
                new ChatMessageRequestDto(1L, 1L, "contents");

        given(chatMessageRepository.save(any())).willReturn(chatMessage);
        given(playerService.findNicknameByPlayerId(anyLong())).willReturn(any());

        chatService.saveMessage(chatMessageRequestDto);

        verify(chatMessageRepository).save(any());
    }
}