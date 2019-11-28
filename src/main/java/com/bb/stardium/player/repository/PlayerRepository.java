package com.bb.stardium.player.repository;

import com.bb.stardium.player.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    public Player findByEmail(final String email);
}
