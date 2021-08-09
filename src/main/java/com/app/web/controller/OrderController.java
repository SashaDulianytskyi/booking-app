package com.app.web.controller;

import com.app.data.entity.RoomOrder;
import com.app.service.OrderService;
import com.app.web.dto.OrderDto;
import com.app.web.validation.OrderValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "/client/{clientId}/room/{roomId}/days/{days}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> create(@RequestBody RoomOrder roomOrder, @PathVariable Long clientId,
                                           @PathVariable Long roomId, @PathVariable int days) {
        OrderValidator.validate(roomOrder);
        this.orderService.create(roomOrder, clientId, roomId, days);
        return new ResponseEntity<>(OrderDto.from(roomOrder), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAll() {
        List<OrderDto> orders = new ArrayList<>();
        this.orderService.findAll().forEach(order -> orders.add(OrderDto.from(order)));

        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        OrderDto orderDto = OrderDto.from(orderService.findById(id));
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderDto>> getAllByClient(@PathVariable Long clientId) {
        List<OrderDto> orders = new ArrayList<>();
        this.orderService.findAllByClient(clientId).forEach(order -> orders.add(OrderDto.from(order)));

        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
