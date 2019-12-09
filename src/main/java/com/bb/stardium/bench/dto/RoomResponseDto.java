package com.bb.stardium.bench.dto;

import com.bb.stardium.player.domain.Player;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
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
}
