package com.app.service.impl;

import com.app.data.dao.ClientDAO;
import com.app.data.entity.Client;
import com.app.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientDAO clientDAO;

    @Autowired
    public ClientServiceImpl(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    @Override
    public Client findById(Long id) {
        return this.clientDAO.findById(id);
    }

    @Override
    public Client save(Client client) {
        return this.clientDAO.save(client);
    }

    @Override
    public List<Client> findAll() {
        return this.clientDAO.findAll();
    }

    @Override
    public void delete(Long id) {
        this.clientDAO.delete(id);
    }

    @Override
    public Client update(Client client) {
        return this.clientDAO.update(client);
    }
}
