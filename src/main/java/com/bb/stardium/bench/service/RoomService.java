package com.bb.stardium.bench.service;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.domain.repository.RoomRepository;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.dto.RoomResponseDto;
import com.bb.stardium.bench.service.exception.MasterAndRoomNotMatchedException;
import com.bb.stardium.bench.service.exception.NotFoundRoomException;
import com.bb.stardium.player.domain.Player;
import com.bb.stardium.player.domain.repository.PlayerRepository;
import com.bb.stardium.player.service.PlayerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
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

    public long create(RoomRequestDto roomRequest, Player player) {
        Room room = toEntity(roomRequest, player);
        room.addPlayer(player);
        Room saveRoom = roomRepository.save(room);
        return saveRoom.getId();
    }

    public long update(long roomId, RoomRequestDto roomRequestDto, final Player player) {
        Room room = roomRepository.findById(roomId).orElseThrow(NotFoundRoomException::new);
        checkRoomMaster(player, room);

        room.update(toEntity(roomRequestDto, player));
        return room.getId();
    }

    private void checkRoomMaster(Player player, Room room) {
        if (room.isNotMaster(player)) {
            throw new MasterAndRoomNotMatchedException();
        }
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

    private Room toEntity(RoomRequestDto roomRequestDto, Player player) {
        return Room.builder()
                .title(roomRequestDto.getTitle())
                .intro(roomRequestDto.getIntro())
                .address(roomRequestDto.getAddress())
                .startTime(roomRequestDto.getStartTime())
                .endTime(roomRequestDto.getEndTime())
                .playersLimit(roomRequestDto.getPlayersLimit())
                .master(player)
                .players(new ArrayList<>())
                .build();
    }

    private List<RoomResponseDto> toResponseDtos(List<Room> rooms) {
        return rooms.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // TODO : service에서 뷰를 고려해서 만들어 보내주는 게 어색함. 리팩토링 필요할듯
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
                .build();
    }

    public void join(String email, Long roomId) {
        Player player = playerService.findByPlayerEmail(email);
        Room room = findRoom(roomId);

        room.addPlayer(player);
        player.addRoom(room);
    }

    public Room quit(String email, Long roomId) {
        Player player = playerService.findByPlayerEmail(email);
        Room room = findRoom(roomId);

        room.removePlayer(player);
        return player.removeRoom(room);
    }

    private boolean isUnexpiredRoom(Room room) {
        return room.getStartTime().isAfter(LocalDateTime.now());
    }

    private boolean hasRemainingSeat(Room room) {
        return (room.getPlayersLimit() - room.getPlayers().size()) > 0;
    }

    public List<RoomResponseDto> findAllUnexpiredRooms() {
        return roomRepository.findAll().stream()
                .filter(this::isUnexpiredRoom)
                .filter(this::hasRemainingSeat)
                .sorted(Comparator.comparing(Room::getStartTime)) // TODO: 추후 추출? 혹은 쿼리 등 다른 방법?
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<RoomResponseDto> findPlayerJoinedRoom(Player player) {
        return roomRepository.findByPlayers_Email(player.getEmail()).stream()
                .sorted(Comparator.comparing(Room::getStartTime))
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
