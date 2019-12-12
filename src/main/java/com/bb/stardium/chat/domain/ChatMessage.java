package com.bb.stardium.chat.domain;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @Column(name="room_id", nullable = false)
    private long roomId;

    @Column(name="player_id", nullable = false)
    private long playerId;

    @Column(name="player_nickname", length = 64, nullable = false)
    private String playerNickname;

    @Lob
    @Column(name="contents", nullable = false)
    private String contents;

    @Column(name="timestamp")
    private OffsetDateTime timestamp;
}
