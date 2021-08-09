package com.app.web.dto;

import com.app.data.entity.Client;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClientDto {
    private String name;
    private String lastName;
    private String phoneNumber;

    public static ClientDto from(Client client) {
        return ClientDto.builder()
                .name(client.getName())
                .lastName(client.getLastName())
                .phoneNumber(client.getPhoneNumber())
                .build();
    }
}
