package com.app.web.dto;

import com.app.data.entity.RoomOrder;
import com.app.data.entity.enums.PaymentMethod;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Builder
@Data
public class OrderDto {
    private PaymentMethod paymentMethod;
    private Date checkInDate;
    private Date checkOutDate;
    private String client;
    private String room;

    public static OrderDto from(RoomOrder roomOrder) {
        return OrderDto.builder()
                .checkInDate(roomOrder.getCInDate())
                .checkOutDate(roomOrder.getCOutDate())
                .paymentMethod(roomOrder.getPaymentMethod())
                .client(roomOrder.getClient().getName()
                        + " " + roomOrder.getClient().getLastName())
                .room(roomOrder.getRoom().getNumber().toString())
                .build();
    }

}
