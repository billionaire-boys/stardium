package com.bb.stardium.player.domain;


import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.mediafile.domain.MediaFile;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private OffsetDateTime createDateTime;

    @UpdateTimestamp
    private OffsetDateTime updateDateTime;

    @Column(name = "nickname", length = 64, nullable = false, unique = true)
    private String nickname;

    @Column(name = "email", length = 64, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @Builder.Default
    @Column(name = "statusMessage", length = 255)
    private String statusMessage = "";

    @Builder.Default
    @ManyToMany(mappedBy = "players")
    private List<Room> rooms = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    private MediaFile profile;

    public void updateStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Player update(final Player newPlayer) {
        this.nickname = newPlayer.nickname;
        this.email = newPlayer.email;
        this.password = newPlayer.password;
        this.statusMessage = newPlayer.statusMessage;
        this.profile = newPlayer.profile;
        return this;
    }

    public boolean isMatchPassword(final String password) {
        return this.password.equals(password);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public Room removeRoom(Room room) {
        rooms.remove(room);
        return room;
    }

    public boolean isSamePlayer(final long playerId, final String email) {
        return this.id == playerId && this.email.equals(email);
    }
}
