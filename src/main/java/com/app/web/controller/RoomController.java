package com.app.web.controller;

import com.app.data.entity.Room;
import com.app.data.entity.enums.Status;
import com.app.exception.NotFoundException;
import com.app.service.RoomService;
import com.app.web.dto.RoomDto;
import com.app.web.validation.RoomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Room> create(@RequestBody Room room) {
        RoomValidator.validate(room);
        roomService.save(room);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Room>> getAll() {
        List<RoomDto> roomDtoList = new ArrayList<>();
        this.roomService.findAll().forEach(room -> roomDtoList.add(RoomDto.from(room)));

        if (roomDtoList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(roomService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomDto> getById(@PathVariable Long id) {
        RoomDto roomDto = RoomDto.from(roomService.findById(id));
        return new ResponseEntity<>(roomDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        roomService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Room> update(@RequestBody Room room) {
        if (room.getId() == null) {
            throw new NotFoundException("Room ID not found, ID is required for update the data");
        }
        return new ResponseEntity<>(roomService.update(room), HttpStatus.OK);
    }

    @GetMapping(value = "/client/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoomDto>> getAllByRenter(@PathVariable Long id) {
        List<RoomDto> rooms = new ArrayList<>();
        this.roomService.findRoomByRenterId(id).forEach(room -> rooms.add(RoomDto.from(room)));
        if (rooms.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping(value = "/available={choice}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoomDto>> getRoomsByStatus(@PathVariable Boolean choice) {
        List<RoomDto> rooms = new ArrayList<>();
        if (choice) {
            this.roomService.findAllByStatus(Status.AVAILABLE).forEach(room -> rooms.add(RoomDto.from(room)));
            return rooms.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(rooms, HttpStatus.OK);
        }
        this.roomService.findAllByStatus(Status.BOOKED).forEach(room -> rooms.add(RoomDto.from(room)));
        return rooms.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(rooms, HttpStatus.OK);


    }
}
