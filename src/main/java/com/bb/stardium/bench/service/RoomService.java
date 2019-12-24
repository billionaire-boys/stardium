package com.bb.stardium.bench.service;

import com.bb.stardium.bench.domain.Room2;
import com.bb.stardium.bench.domain.repository.Room2Repository;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.dto.RoomResponseDto;
import com.bb.stardium.bench.service.exception.AlreadyJoinedException;
import com.bb.stardium.bench.service.exception.FixedReadyRoomException;
import com.bb.stardium.bench.service.exception.MasterAndRoomNotMatchedException;
import com.bb.stardium.bench.service.exception.NotFoundRoomException;
import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.domain.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class RoomService {
    private final Room2Repository roomRepository;
    private final PlayerRepository playerRepository;

    public long create(RoomRequestDto roomRequest, Player master) {
        Room2 room = roomRequest.toEntity(master);
        room.addPlayer(master);
        Room2 saveRoom = roomRepository.save(room);
        return saveRoom.getId();
    }

    public long update(long roomId, RoomRequestDto roomRequestDto, final Player player) {
        Room2 room = roomRepository.findById(roomId).orElseThrow(NotFoundRoomException::new);
        checkRoomMaster(player, room);

        room.update(roomRequestDto.toEntity(player));
        return room.getId();
    }

    private void checkRoomMaster(Player player, Room2 room) {
        if (room.isNotMaster(player)) {
            throw new MasterAndRoomNotMatchedException();
        }
    }

    public boolean delete(long roomId, Player loggedInPlayer) {
        Room2 room = roomRepository.findById(roomId).orElseThrow(NotFoundRoomException::new);
        if (room.isNotMaster(loggedInPlayer)) {
            throw new MasterAndRoomNotMatchedException();
        }
        if (room.isReady()) {
            throw new FixedReadyRoomException();
        }
        roomRepository.delete(room);
        return true;
    }

    @Transactional(readOnly = true)
    public Room2 findRoom(long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(NotFoundRoomException::new);
    }

    public List<RoomResponseDto> findAllRooms() { // TODO: 필요한지 논의
        List<Room2> rooms = roomRepository.findAll();
        return toResponseDtos(rooms);
    }

    private List<RoomResponseDto> toResponseDtos(List<Room2> rooms) {
        return rooms.stream()
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());
    }

    public Room2 join(Player loggedInPlayer, Long roomId) {
        Room2 room = findRoom(roomId);
        if (room.hasPlayer(loggedInPlayer)) {
            throw new AlreadyJoinedException();
        }

        room.addPlayer(loggedInPlayer);
        return room;
    }

    public void quit(long roomId, Player loggedInPlayer) {
        Room2 room = findRoom(roomId);

        if (room.isReady()) {
            throw new FixedReadyRoomException();
        }

        room.removePlayer(loggedInPlayer);
    }

    @Transactional(readOnly = true)
    public List<RoomResponseDto> findAllUnexpiredRooms() {
        return roomRepository.findAll().stream()
                .filter(Room2::isUnexpiredRoom)
                .filter(Room2::hasRemainingSeat)
                .sorted(Comparator.comparing(Room2::getStartTime)) // TODO: 추후 추출? 혹은 쿼리 등 다른 방법?
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomResponseDto> findPlayerJoinedRoom(Player player) {
        return roomRepository.findByPlayersOrderByStartTimeAsc(player).stream()
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomResponseDto> findRoomsFilterBySection(String section) {
        Sort sort = Sort.by(Sort.Direction.ASC, "start_time");
        return roomRepository.findAllByAddressSection(section, sort).stream()
                .filter(Room2::isUnexpiredRoom)
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<RoomResponseDto> findRoomBySearchKeyword(String searchKeyword) {
        return roomRepository.findAllByDescriptionTitleContaining(searchKeyword).stream()
                .filter(Room2::isUnexpiredRoom)
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());
    }

}
