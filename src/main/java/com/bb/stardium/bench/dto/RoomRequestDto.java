package com.bb.stardium.bench.dto;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.common.util.EscapedCharacters;
import com.bb.stardium.player.domain.Player;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class RoomRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String intro;

    private Address address;

    @Future
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @Future
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @Min(value = 2)
    private int playersLimit;

    private Player master;

    @Builder
    public RoomRequestDto(@NotBlank String title, @NotBlank String intro, Address address, @Future LocalDateTime startTime, @Future LocalDateTime endTime, @Min(value = 2) int playersLimit, Player master) {
        this.title = title;
        this.intro = intro;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
        this.playersLimit = playersLimit;
        this.master = master;
    }

    public Room toEntity(Player player) {
        return Room.builder()
                .title(EscapedCharacters.of(this.title))
                .intro(EscapedCharacters.of(this.intro))
                .address(this.address)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .playersLimit(this.playersLimit)
                .master(player)
                .build();
    }
}
