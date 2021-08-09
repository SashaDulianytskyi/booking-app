package com.app.data.dao;

import com.app.data.entity.Client;
import com.app.data.entity.Room;
import com.app.data.entity.enums.Status;

import java.util.List;
import java.util.Optional;

public interface RoomDAO {
    Room findById(Long id);

    Room save(Room room);

    List<Room> findAll();

    void delete(Long id);

    Room update(Room room);

    List<Room> findRoomsByClient(Client client);

    List<Room> findAllByStatus(Status status);
}
