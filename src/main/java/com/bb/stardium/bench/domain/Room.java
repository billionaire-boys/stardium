package com.bb.stardium.bench.domain;

import com.bb.stardium.bench.dto.RoomRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
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

    @Future
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;

    private int playersLimit;

    public void update(Room updatedRoom) {
        this.title = updatedRoom.getTitle();
        this.intro = updatedRoom.getIntro();
        this.address = updatedRoom.getAddress();
        this.startTime = updatedRoom.getStartTime();
        this.endTime = updatedRoom.getEndTime();
        this.playersLimit = updatedRoom.getPlayersLimit();
    }
}
