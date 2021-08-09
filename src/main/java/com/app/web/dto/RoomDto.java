package com.app.web.dto;

import com.app.data.entity.Room;
import com.app.data.entity.enums.RoomType;
import com.app.data.entity.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomDto {
    private int number;
    private int totalPlaces;
    private int price;
    private RoomType roomType;
    private Status status;
    private String renter;

    public static RoomDto from(Room room) {
        return RoomDto.builder()
                .number(room.getNumber())
                .price(room.getPrice())
                .roomType(room.getRoomType())
                .totalPlaces(room.getTotalPlaces())
                .status(room.getStatus())
                .renter(room.getRenter().getName()
                        + " " + room.getRenter().getLastName())
                .build();
    }
}
