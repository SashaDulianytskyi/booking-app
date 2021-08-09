package com.app.service;

import com.app.data.entity.Room;
import com.app.data.entity.enums.Status;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    Room findById(Long id);

    Room save(Room room);

    List<Room> findAll();

    void delete(Long id);

    Room update(Room room);

    List<Room> findRoomByRenterId(Long id);

    List<Room> findAllByStatus(Status status);
}
