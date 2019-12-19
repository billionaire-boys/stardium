package com.bb.stardium.chat.service;

import com.bb.stardium.chat.domain.ChatMessage;
import com.bb.stardium.chat.dto.ChatMessageRequestDto;
import com.bb.stardium.chat.dto.ChatMessageResponseDto;
import com.bb.stardium.chat.repository.ChatMessageRepository;
import com.bb.stardium.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {

    private final PlayerService playerService;
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(final ChatMessageRequestDto requestDto) {
        return chatMessageRepository.save(getMessageWithAdditionalInfo(requestDto));
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponseDto> getPreviousChatMessages(final long roomId) {
        List<ChatMessage> prevMessages = chatMessageRepository.findAllByRoomIdOrderByTimestamp(roomId);

        return prevMessages.stream()
                .map(ChatMessageResponseDto::new)
                .collect(Collectors.toList());
    }

    private ChatMessage getMessageWithAdditionalInfo(final ChatMessageRequestDto requestDto) {
        final String nickname = playerService.findNicknameByPlayerId(requestDto.getPlayerId());
        return requestDto.toEntity(nickname, OffsetDateTime.now());
    }

}
