package com.bb.stardium.player.domain;

import com.bb.stardium.bench.domain.Room;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private OffsetDateTime createdDateTime;

    @UpdateTimestamp
    private OffsetDateTime updatedDateTime;

    @Column(name = "nickname", length = 64, nullable = false, unique = true)
    private String nickname;

    @Column(name = "email", length = 64, nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @Column(name = "statusMessage")
    private String statusMessage = "";

    @ManyToMany(mappedBy = "players")
    private List<Room> rooms = new ArrayList<>();

    protected Player() {
        this.updatedDateTime = OffsetDateTime.now();
    }

    public Player(final String nickname, final String email, final String password) {
        this();
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public Player(final String nickname, final String email, final String password, final String statusMessage) {
        this(nickname, email, password);
        this.statusMessage = statusMessage;
    }

    public Player update(final Player newPlayer) {
        this.nickname = newPlayer.nickname;
        this.email = newPlayer.email;
        this.password = newPlayer.password;
        this.statusMessage = newPlayer.statusMessage;
        this.updatedDateTime = OffsetDateTime.now();
        return this;
    }

    public boolean isMatchPassword(final String password) {
        return this.password.equals(password);
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }
}
