package com.bb.stardium.bench.domain.repository;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.player.domain.Player;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Room2Repository extends JpaRepository<Room, Long> {
    List<Room> findByPlayersOrderByStartTimeAsc(Player player);

    List<Room> findAllByAddressSectionOrderByStartTimeAsc(final String section);

    List<Room> findAllByDescriptionTitleContaining(final String searchKeyword);
}
