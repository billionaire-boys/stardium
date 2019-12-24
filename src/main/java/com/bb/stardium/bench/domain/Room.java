package com.bb.stardium.bench.domain;

import com.bb.stardium.bench.domain.exception.PlayerAlreadyExistException;
import com.bb.stardium.player.domain.Player;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Room {

    private static final int EMPTY_SEAT = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private String title;
    private String intro;
    private int playersLimit;

    @Embedded
    private Address address;

    @Future
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;


    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id")
    private Player master;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "player_room",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private List<Player> players = new ArrayList<>();

    public void update(Room updatedRoom) {
        this.title = updatedRoom.getTitle();
        this.intro = updatedRoom.getIntro();
        this.address = updatedRoom.getAddress();
        this.startTime = updatedRoom.getStartTime();
        this.endTime = updatedRoom.getEndTime();
        this.playersLimit = updatedRoom.getPlayersLimit();
    }

    public boolean isNotMaster(Player masterPlayer) {
        return this.master != masterPlayer;
    }

    public void addPlayer(Player player) {
        if (hasPlayer(player)) {
            throw new PlayerAlreadyExistException();
        }
        this.players.add(player);
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    public boolean isUnexpiredRoom() {
        return this.getStartTime().isAfter(LocalDateTime.now());
    }

    public boolean hasRemainingSeat() {
        return this.playersLimit - players.size() > EMPTY_SEAT;
    }

    public boolean isReady() {
        return !hasRemainingSeat();
    }
}
