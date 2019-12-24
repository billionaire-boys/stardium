package com.bb.stardium.bench.domain.repository;

import com.bb.stardium.bench.domain.Room2;
import com.bb.stardium.player.domain.Player;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Room2Repository extends JpaRepository<Room2, Long> {
    List<Room2> findByPlayersOrderByStartTimeAsc(Player player);

    List<Room2> findAllByAddressSectionOrderByStartTimeAsc(final String section);

    List<Room2> findAllByAddressSection(String section, Sort sortable);

    List<Room2> findAllByDescriptionTitleContaining(final String searchKeyword);
}
