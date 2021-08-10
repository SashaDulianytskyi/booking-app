package com.app.web.controller;

import com.app.data.entity.Client;
import com.app.exception.NotFoundException;
import com.app.service.ClientService;
import com.app.web.dto.ClientDto;
import com.app.web.validation.ClientValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> create(@RequestBody Client client) {
        ClientValidator.validate(client);
        this.clientService.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDto>> getAll() {
        List<ClientDto> clients = new ArrayList<>();
        this.clientService.findAll().forEach(client -> clients.add(ClientDto.from(client)));

        if (clients.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> getById(@PathVariable Long id) {
        ClientDto clientDto = ClientDto.from(clientService.findById(id));
        return new ResponseEntity<>(clientDto, HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> update(@RequestBody Client client) {
        return new ResponseEntity<>(this.clientService.update(client), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        this.clientService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
