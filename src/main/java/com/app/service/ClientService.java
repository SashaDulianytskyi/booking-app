package com.app.service;

import com.app.data.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client findById(Long id);

    Client save(Client client);

    List<Client> findAll();

    void delete(Long id);

    Client update(Client client);

}
