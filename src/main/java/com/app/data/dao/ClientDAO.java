package com.app.data.dao;

import com.app.data.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientDAO {
    Client findById(Long id);

    Client save(Client client);

    List<Client> findAll();

    void delete(Long id);

    Client update(Client client);
}
