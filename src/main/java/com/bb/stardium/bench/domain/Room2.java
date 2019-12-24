package com.bb.stardium.bench.domain;

import com.bb.stardium.player.domain.Player;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Room2 {
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

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "room_player",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private List<Player> players = new ArrayList<>();

    @Builder
    public Room2(String title, String intro, int playersLimit, Address address, @Future LocalDateTime startTime, @Future LocalDateTime endTime, Player master) {
        this.title = title;
        this.intro = intro;
        this.playersLimit = playersLimit;
        this.address = address;
        this.startTime = startTime;
        this.endTime = endTime;
        this.master = master;
    }
}
