package com.app.data.dao.impl;

import com.app.data.dao.RoomDAO;
import com.app.data.dao.repository.RoomRepository;
import com.app.data.entity.Client;
import com.app.data.entity.Room;
import com.app.data.entity.enums.Status;
import com.app.exception.DBException;
import com.app.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RoomDaoImpl implements RoomDAO {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomDaoImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room findById(Long id) {
        return this.roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Room with id %s is not found", id)));
    }

    @Override
    public Room save(Room room) {
        Room createdRoom;
        try {
            createdRoom = this.roomRepository.save(room);
        } catch (DataIntegrityViolationException e) {
            log.error("Room creation failed");
            throw new DBException("Room is already exist");
        }
         return createdRoom;
    }

    @Override
    public List<Room> findAll() {
        return (List<Room>) this.roomRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        this.roomRepository.deleteById(id);
    }

    @Override
    public Room update(Room room) {
        Room updatedRoom;
        try {
            updatedRoom = this.roomRepository.save(room);
        } catch (DataIntegrityViolationException e) {
            log.error("Room update failed");
            throw new DBException("Room update failed");
        }
        return updatedRoom;
    }

    @Override
    public List<Room> findRoomsByClient(Client client) {
        return this.roomRepository.findRoomsByRenter(client);
    }

    @Override
    public List<Room> findAllByStatus(Status status) {
        return this.roomRepository.findAllByStatus(status);
    }
}
