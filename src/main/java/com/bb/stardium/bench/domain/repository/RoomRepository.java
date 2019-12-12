package com.bb.stardium.bench.domain.repository;

import com.bb.stardium.bench.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    // TODO : player로 찾는 걸로 수정
    List<Room> findByPlayers_Email(final String email);

    List<Room> findAllByAddressSectionOrderByStartTimeAsc(final String section);

    List<Room> findAllByTitleContaining(final String searchKeyword);
}
