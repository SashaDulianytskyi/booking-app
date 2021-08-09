package com.app.data.dao.impl;

import com.app.data.dao.RoomOrderDAO;
import com.app.data.dao.repository.RoomOrderRepository;
import com.app.data.entity.Client;
import com.app.data.entity.RoomOrder;
import com.app.exception.DBException;
import com.app.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RoomOrderDaoImpl implements RoomOrderDAO {
    private final RoomOrderRepository roomOrderRepository;

    @Autowired
    public RoomOrderDaoImpl(RoomOrderRepository roomOrderRepository) {
        this.roomOrderRepository = roomOrderRepository;
    }

    @Override
    public RoomOrder findById(Long id) {
        return this.roomOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Order with id %s is not found", id)));
    }

    @Override
    public List<RoomOrder> findByClient(Client client) {
        return this.roomOrderRepository.findAllByClient(client);
    }

    @Override
    public RoomOrder save(RoomOrder roomOrder) {
        RoomOrder createdRoomOrder;
        try {
            createdRoomOrder = this.roomOrderRepository.save(roomOrder);
        } catch (DataIntegrityViolationException e) {
            log.error("Order creation failed");
            throw new DBException("Order is already exist");
        }
        return createdRoomOrder;
    }

    @Override
    public List<RoomOrder> findAll() {
        return (List<RoomOrder>) this.roomOrderRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        this.roomOrderRepository.deleteById(id);
    }

    @Override
    public RoomOrder update(RoomOrder roomOrder) {
        RoomOrder updatedRoomOrder;
        try {
            updatedRoomOrder = this.roomOrderRepository.save(roomOrder);
        } catch (DataIntegrityViolationException e) {
            log.error("Order update failed");
            throw new DBException("Order update failed");
        }
        return updatedRoomOrder;
    }
}
