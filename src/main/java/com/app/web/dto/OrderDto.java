package com.app.web.dto;

import com.app.data.entity.RoomOrder;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Builder
@Data
public class OrderDto {
    private String paymentMethod;
    private Date checkInDate;
    private Date checkOutDate;
    private String client;
    private int room;

    public static OrderDto from(RoomOrder roomOrder) {
        return OrderDto.builder()
                .checkInDate(roomOrder.getCInDate())
                .checkOutDate(roomOrder.getCInDate())
                .client(roomOrder.getClient().getName()
                        + " " + roomOrder.getClient().getLastName())
                .build();
    }

}
