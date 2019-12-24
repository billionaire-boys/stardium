package com.bb.stardium.player.domain.repository;

import com.bb.stardium.player.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Player> findByEmail(String email);
}
