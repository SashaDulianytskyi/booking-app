package com.app.service.impl;

import com.app.data.dao.ClientDAO;
import com.app.data.dao.RoomDAO;
import com.app.data.dao.RoomOrderDAO;
import com.app.data.entity.Client;
import com.app.data.entity.Room;
import com.app.data.entity.RoomOrder;
import com.app.data.entity.enums.Status;
import com.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final ClientDAO clientDAO;
    private final RoomOrderDAO roomOrderDAO;
    private final RoomDAO roomDAO;

    @Autowired
    public OrderServiceImpl(ClientDAO clientDAO, RoomOrderDAO roomOrderDAO, RoomDAO roomDAO) {
        this.clientDAO = clientDAO;
        this.roomOrderDAO = roomOrderDAO;
        this.roomDAO = roomDAO;
    }

    @Override
    public RoomOrder findById(Long id) {
        return this.roomOrderDAO.findById(id);
    }

    @Override
    public RoomOrder create(RoomOrder roomOrder, Long clientId, Long roomId, int bookingDays) {
        Client client = this.clientDAO.findById(clientId);
        Room room = this.roomDAO.findById(roomId);

        client.getRoomOrders().add(roomOrder);
        room.setStatus(Status.BOOKED);

        roomOrder.setClient(client);
        roomOrder.setRoom(room);
        roomOrder.setCInDate(new Date(System.currentTimeMillis()));
        roomOrder.setCOutDate(new Date(System.currentTimeMillis() + DayConverter.convertDaysToMillis(bookingDays)));

        this.clientDAO.update(client);

        return roomOrder;
    }

    @Override
    public List<RoomOrder> findAll() {
        return this.roomOrderDAO.findAll();
    }

    @Override
    public List<RoomOrder> findAllByClient(Long id) {
        Client client = clientDAO.findById(id);
        return this.roomOrderDAO.findByClient(client);
    }

    private static class DayConverter {
        public static long convertDaysToMillis(int day) {
            return day * 8640000L;
        }
    }
}
