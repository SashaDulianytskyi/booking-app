package com.app.data.dao;

import com.app.data.entity.Client;
import com.app.data.entity.RoomOrder;

import java.util.List;
import java.util.Optional;

public interface RoomOrderDAO {
    RoomOrder findById(Long id);

    List<RoomOrder> findByClient(Client client);

    RoomOrder save(RoomOrder roomOrder);

    List<RoomOrder> findAll();

    void delete(Long id);

    RoomOrder update(RoomOrder roomOrder);
}
