package com.bb.stardium.bench.dto;

import com.bb.stardium.player.domain.Player;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RoomResponseDto {
    @NotBlank
    private long id;

    @NotBlank
    private String title;

    @NotBlank
    private String intro;

    @NotBlank
    private String address;

    @NotBlank
    private String playTime;

    @NotBlank
    private int playLimits;

    @NotBlank
    private int playerCount;

    @NotNull
    private Player master;

    @Builder
    public RoomResponseDto(@NotBlank long id, @NotBlank String title,
                           @NotBlank String intro, @NotBlank String address,
                           @NotBlank String playTime, @NotBlank int playLimits,
                           @NotBlank int playerCount, @NotNull Player master) {
        this.id = id;
        this.title = title;
        this.intro = intro;
        this.address = address;
        this.playTime = playTime;
        this.playLimits = playLimits;
        this.playerCount = playerCount;
        this.master = master;
    }
}
