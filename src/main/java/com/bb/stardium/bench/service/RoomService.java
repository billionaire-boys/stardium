package com.bb.stardium.bench.service;

import com.bb.stardium.bench.domain.Room;
import com.bb.stardium.bench.domain.repository.RoomRepository;
import com.bb.stardium.bench.dto.RoomRequestDto;
import com.bb.stardium.bench.service.exception.NotFoundRoomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public long create(RoomRequestDto roomRequest) {
        Room saveRoom = roomRepository.save(roomRequest.toEntity());
        return saveRoom.getId();
    }

    public long update(long roomId, RoomRequestDto roomRequestDto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(NotFoundRoomException::new);
        room.update(roomRequestDto);
        return room.getId();
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

}
