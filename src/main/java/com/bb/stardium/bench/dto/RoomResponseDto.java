package com.bb.stardium.bench.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Builder
@Getter @Setter
@EqualsAndHashCode
public class RoomResponseDto {
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
}
