package com.app.service;

import com.app.data.entity.RoomOrder;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    RoomOrder findById(Long id);

    RoomOrder create(RoomOrder roomOrder, Long clientId, Long roomId, int bookingDays);

    List<RoomOrder> findAll();

    List<RoomOrder> findAllByClient(Long id);
}
