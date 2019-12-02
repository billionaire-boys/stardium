package com.bb.stardium.player.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private OffsetDateTime createdDateTime;

    @UpdateTimestamp
    private OffsetDateTime updatedDateTime;

    @Column(name = "nickname", length = 32, nullable = false)
    private String nickname;

    @Column(name = "email", length = 64, nullable = false)
    private String email;

    @Column(name = "password", length = 64, nullable = false)
    private String password;

    private Player() {
        this.updatedDateTime = OffsetDateTime.now();
    }

    public Player(final String nickname, final String email, final String password) {
        this();
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public OffsetDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public OffsetDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    @Override
    public boolean equals(final Object another) {
        if (this == another) return true;
        if (another == null || getClass() != another.getClass()) return false;
        final Player player = (Player) another;
        return Objects.equals(email, player.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Player {" +
                "nickname: \"" + nickname + "\"" +
                ", email: \"" + email + "\"" +
                ", databaseId: \"" + id + "\"" +
                ", createdDateTime: \"" + createdDateTime + "\"" +
                ", updatedDateTime: \"" + updatedDateTime + "\"" +
                "}";
    }
}
