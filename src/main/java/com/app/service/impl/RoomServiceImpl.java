package com.app.service.impl;

import com.app.data.dao.ClientDAO;
import com.app.data.dao.RoomDAO;
import com.app.data.entity.Client;
import com.app.data.entity.Room;
import com.app.data.entity.enums.Status;
import com.app.exception.NotFoundException;
import com.app.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {
    private final RoomDAO roomDAO;
    private final ClientDAO clientDAO;

    @Autowired
    public RoomServiceImpl(RoomDAO roomDAO, ClientDAO clientDAO) {
        this.roomDAO = roomDAO;
        this.clientDAO = clientDAO;
    }

    @Override
    public Room findById(Long id) {
        return this.roomDAO.findById(id);
    }

    @Override
    public Room save(Room room) {
        return this.roomDAO.save(room);
    }

    @Override
    public List<Room> findAll() {
        return this.roomDAO.findAll();
    }

    @Override
    public void delete(Long id) {
        this.roomDAO.delete(id);
    }

    @Override
    public Room update(Room room) {
        return this.roomDAO.update(room);
    }

    public List<Room> findRoomByRenterId(Long id){
        Client client = clientDAO.findById(id);
        return this.roomDAO.findRoomsByClient(client);
    }

    @Override
    public List<Room> findAllByStatus(Status status) {
        return this.roomDAO.findAllByStatus(status);
    }
}
