package com.bb.stardium.bench.dto;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.player.domain.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;

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

    public RoomResponseDto (Room room) {
        this.title = room.getTitle();
        this.intro = room.getIntro();
        this.address = String.format("%s %s %s",
                room.getAddress().getCity(),
                room.getAddress().getSection(),
                room.getAddress().getDetail());
        this.playTime = String.format("%s - %s",
                room.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                room.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        this.playLimits = room.getPlayersLimit();
        this.master = room.getMaster();
        this.id = room.getId();
        this.playerCount = room.getPlayers().size();
    }
}
