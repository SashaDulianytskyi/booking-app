package com.app.data.dao.impl;

import com.app.data.dao.ClientDAO;
import com.app.data.dao.repository.ClientRepository;
import com.app.data.entity.Client;
import com.app.exception.DBException;
import com.app.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ClientDaoImpl implements ClientDAO {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientDaoImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client findById(Long id) {
        return this.clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Client with id '%s' is not found", id)));
    }

    @Override
    public Client save(Client client) {
        Client createdClient;
        try {
            createdClient = this.clientRepository.save(client);
        } catch (DataIntegrityViolationException e) {
            log.error("Client creation failed");
            throw new DBException("Client is already exist");
        }
        return createdClient;
    }

    @Override
    public List<Client> findAll() {
        return (List<Client>) this.clientRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        this.clientRepository.deleteById(id);

    }

    @Override
    public Client update(Client client) {
        Client updatedClient;
        try {
            updatedClient = this.clientRepository.save(client);
        } catch (DataIntegrityViolationException e) {
            log.error("Client update failed");
            throw new DBException("Client update failed");
        }
        return updatedClient;
    }
}
