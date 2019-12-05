package com.bb.stardium.bench.domain;

import com.bb.stardium.player.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id")
    private Player master;

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

    public Player addPlayer(Player player) {
        players.add(player);
        return player;
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player);
    }

    public Player removePlayer(Player player) {
        players.remove(player);
        return player;
    }
}
