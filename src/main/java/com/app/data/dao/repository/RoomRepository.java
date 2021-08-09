package com.app.data.dao.repository;

import com.app.data.entity.Client;
import com.app.data.entity.Room;
import com.app.data.entity.enums.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    List<Room> findAllByStatus(Status status);
    List<Room> findRoomsByRenter(Client client);
}
