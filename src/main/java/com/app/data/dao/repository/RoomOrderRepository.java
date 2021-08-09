package com.app.data.dao.repository;

import com.app.data.entity.Client;
import com.app.data.entity.RoomOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomOrderRepository extends CrudRepository<RoomOrder, Long> {
    List<RoomOrder> findAllByClient(Client client);
}
