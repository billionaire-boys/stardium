package com.bb.stardium.bench.service;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.domain.repository.RoomRepository;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.dto.RoomResponseDto;
import com.bb.stardium.bench.service.exception.AlreadyJoinedException;
import com.bb.stardium.bench.service.exception.FixedReadyRoomException;
import com.bb.stardium.bench.service.exception.MasterAndRoomNotMatchedException;
import com.bb.stardium.bench.service.exception.NotFoundRoomException;
import com.bb.stardium.player.domain.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

    public long create(RoomRequestDto roomRequest, Player player) {
        Room room = roomRequest.toEntity(player);
        room.addPlayer(player);
        Room saveRoom = roomRepository.save(room);
        return saveRoom.getId();
    }

    public long update(long roomId, RoomRequestDto roomRequestDto, final Player player) {
        Room room = roomRepository.findById(roomId).orElseThrow(NotFoundRoomException::new);
        checkRoomMaster(player, room);

        room.update(roomRequestDto.toEntity(player));
        return room.getId();
    }

    private void checkRoomMaster(Player player, Room room) {
        if (room.isNotMaster(player)) {
            throw new MasterAndRoomNotMatchedException();
        }
    }

    public boolean delete(long roomId, Player loggedInPlayer) {
        Room room = roomRepository.findById(roomId).orElseThrow(NotFoundRoomException::new);
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
    public Room findRoom(long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(NotFoundRoomException::new);
    }

    public List<RoomResponseDto> findAllRooms() { // TODO: 필요한지 논의
        List<Room> rooms = roomRepository.findAll();
        return toResponseDtos(rooms);
    }

    private List<RoomResponseDto> toResponseDtos(List<Room> rooms) {
        return rooms.stream()
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());
    }

    public Room join(Player loggedInPlayer, Long roomId) {
        Room room = findRoom(roomId);
        if (room.hasPlayer(loggedInPlayer)) {
            throw new AlreadyJoinedException();
        }

        room.addPlayer(loggedInPlayer);
        return room;
    }

    public void quit(Player loggedInPlayer, Long roomId) {
        Room room = findRoom(roomId);

        if (room.isReady()) {
            throw new FixedReadyRoomException();
        }

        room.removePlayer(loggedInPlayer);
    }

    @Transactional(readOnly = true)
    public List<RoomResponseDto> findAllUnexpiredRooms() {
        return roomRepository.findAll().stream()
                .filter(Room::isUnexpiredRoom)
                .filter(Room::hasRemainingSeat)
                .sorted(Comparator.comparing(Room::getStartTime)) // TODO: 추후 추출? 혹은 쿼리 등 다른 방법?
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomResponseDto> findPlayerJoinedRoom(Player player) {
        return roomRepository.findByPlayers_Email(player.getEmail()).stream()
                .sorted(Comparator.comparing(Room::getStartTime))
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomResponseDto> findRoomsFilterBySection(String section) {
        return roomRepository.findAllByAddressSectionOrderByStartTimeAsc(section).stream()
                .filter(Room::isUnexpiredRoom)
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<RoomResponseDto> findRoomBySearchKeyword(String searchKeyword) {
        return roomRepository.findAllByTitleContaining(searchKeyword).stream()
                .filter(Room::isUnexpiredRoom)
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());
    }

}
