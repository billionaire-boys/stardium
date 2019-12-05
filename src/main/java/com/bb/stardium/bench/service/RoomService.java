package com.bb.stardium.bench.service;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.domain.repository.RoomRepository;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.dto.RoomResponseDto;
import com.bb.stardium.bench.service.exception.NotFoundRoomException;
import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.service.PlayerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomService {
    private final RoomRepository roomRepository;
    private final PlayerService playerService;

    public RoomService(RoomRepository roomRepository, PlayerService playerService) {
        this.roomRepository = roomRepository;
        this.playerService = playerService;
    }

    public long create(RoomRequestDto roomRequest) {
        Room saveRoom = roomRepository.save(roomRequest.toEntity());
        return saveRoom.getId();
    }

    public long update(long roomId, RoomRequestDto roomRequestDto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(NotFoundRoomException::new);
        room.update(toEntity(roomRequestDto));
        return room.getId();
    }

    private Room toEntity(RoomRequestDto roomRequestDto) {
        return Room.builder()
                .title(roomRequestDto.getTitle())
                .intro(roomRequestDto.getIntro())
                .address(roomRequestDto.getAddress())
                .startTime(roomRequestDto.getStartTime())
                .endTime(roomRequestDto.getEndTime())
                .playersLimit(roomRequestDto.getPlayersLimit())
                .build();
    }

    public boolean delete(long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(NotFoundRoomException::new);
        roomRepository.delete(room);
        return true;
    }

    @Transactional(readOnly = true)
    public Room findRoom(long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(NotFoundRoomException::new);
    }

    public List<RoomResponseDto> findAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return toResponseDtos(rooms);
    }

    private List<RoomResponseDto> toResponseDtos(List<Room> rooms) {
        return rooms.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private RoomResponseDto toResponseDto(Room room) {
        return RoomResponseDto.builder()
            .title(room.getTitle())
            .intro(room.getIntro())
            .address(String.format("%s %s %s",
                room.getAddress().getCity(),
                room.getAddress().getSection(),
                room.getAddress().getDetail()))
            .playTime(String.format("%s - %s",
                room.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                room.getEndTime().format(DateTimeFormatter.ofPattern("dd"))))
            .playLimits(room.getPlayersLimit())
                .id(room.getId())
                .playerCount(room.getPlayers().size())
            .build();
    }

    public Room join(String email, Long roomId) {
        Player player = playerService.findByPlayerEmail(email);
        Room room = findRoom(roomId);

        room.addPlayer(player);
        return player.addRoom(room);
    }
}
