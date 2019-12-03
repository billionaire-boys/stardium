package com.bb.stardium.bench.domain;

import com.bb.stardium.bench.dto.RoomRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private String title;

    private String intro;

    @Embedded
    private Address address;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int playersLimit;

    public void update(RoomRequestDto roomRequestDto) {
        this.title = roomRequestDto.getTitle();
        this.intro = roomRequestDto.getIntro();
        this.address = roomRequestDto.getAddress();
        this.startTime = roomRequestDto.getStartTime();
        this.endTime = roomRequestDto.getEndTime();
        this.playersLimit = roomRequestDto.getPlayersLimit();
    }
}
