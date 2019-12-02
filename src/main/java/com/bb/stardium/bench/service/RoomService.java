package com.bb.stardium.bench.service;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.domain.repository.RoomRepository;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.service.exception.NotFoundRoomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoomService {

    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional
    public Long create(RoomRequestDto roomRequest) {
        Room room = toRoomEntity(roomRequest);
        Room saveRoom = roomRepository.save(room);
        return saveRoom.getId();
    }

    @Transactional
    public Long update(Long roomId, RoomRequestDto roomRequestDto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(NotFoundRoomException::new);
        room.update(roomRequestDto);
        return room.getId();
    }

    @Transactional
    public void delete(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(NotFoundRoomException::new);
        roomRepository.delete(room);
    }

    public Room findRoom(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(NotFoundRoomException::new);
    }

    private Room toRoomEntity(RoomRequestDto roomRequest) {
        return Room.builder()
                .title(roomRequest.getTitle())
                .intro(roomRequest.getIntro())
                .address(roomRequest.getAddress())
                .startTime(roomRequest.getStartTime())
                .endTime(roomRequest.getEndTime())
                .playersLimit(roomRequest.getPlayersLimit())
                .build();
    }
}
