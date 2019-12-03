package com.bb.stardium.bench.dto;

import com.bb.stardium.bench.domain.Address;
import com.bb.stardium.bench.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoomRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String intro;

    private Address address;

    @Future
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;

    @Min(value = 2)
    private int playersLimit;

    public Room toEntity() {
        return Room.builder()
                .title(title)
                .intro(intro)
                .address(address)
                .startTime(startTime)
                .endTime(endTime)
                .playersLimit(playersLimit)
                .build();
    }
}
