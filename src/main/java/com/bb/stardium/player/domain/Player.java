package com.bb.stardium.player.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
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
    private String email;

    @Column(name = "password", length = 64, nullable = false)
    private String password;

    protected Player() {
        this.updatedDateTime = OffsetDateTime.now();
    }

    public Player(final String nickname, final String email, final String password) {
        this();
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public Player update(final Player newPlayer) {
        this.nickname = newPlayer.nickname;
        this.email = newPlayer.email;
        this.password = newPlayer.password;
        this.updatedDateTime = newPlayer.updatedDateTime;
        return this;
    }

    public boolean isMatchPassword(final String password) {
        return this.password.equals(password);
    }
}
