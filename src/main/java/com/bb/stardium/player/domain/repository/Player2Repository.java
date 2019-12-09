package com.bb.stardium.player.domain.repository;

import com.bb.stardium.player.domain.Player2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Player2Repository extends JpaRepository<Player2, Long> {
    boolean existsByEmail(String email);

    Optional<Player2> findByEmail(String email);
}
