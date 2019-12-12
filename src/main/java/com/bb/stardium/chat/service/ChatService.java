package com.bb.stardium.chat.service;

import com.bb.stardium.chat.domain.ChatMessage;
import com.bb.stardium.chat.dto.ChatMessageRequestDto;
import com.bb.stardium.chat.dto.ChatMessageResponseDto;
import com.bb.stardium.chat.repository.ChatMessageRepository;
import com.bb.stardium.player.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ChatService {

    private PlayerService playerService;
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(final ChatMessageRequestDto requestDto) {
        final ChatMessage message = getMessage(requestDto);
        chatMessageRepository.save(message);
        return message;
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponseDto> getPreviousChatMessages(final long roomId) {
        List<ChatMessage> prevMessages = chatMessageRepository.findAllByRoomIdOrderByTimestamp(roomId);

        return prevMessages.stream()
                .map(ChatMessageResponseDto::new)
                .collect(Collectors.toList());
    }

    private ChatMessage getMessage(final ChatMessageRequestDto requestDto) {
        // TODO: 데이터 sanitize
        final String nickname = playerService.findNicknameByPlayerId(requestDto.getPlayerId());

        return ChatMessage.builder()
                .roomId(requestDto.getRoomId())
                .playerId(requestDto.getPlayerId())
                .playerNickname(nickname)
                .contents(requestDto.getContents())
                .timestamp(OffsetDateTime.now())
                .build();
    }
}
