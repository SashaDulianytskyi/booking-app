package com.app.web.controller.integration;

import com.app.data.dao.RoomDAO;
import com.app.data.entity.Client;
import com.app.data.entity.Room;
import com.app.data.entity.enums.RoomType;
import com.app.data.entity.enums.Status;
import com.app.web.controller.ControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.web.util.NestedServletException;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RoomControllerIntegrationTest extends ControllerIntegrationTest {
    @Autowired
    private RoomDAO roomDAO;

    private static final Long TEST_ID = 1L;

    private static final Client TEST_CLIENT = Client.builder()
            .id(1L)
            .name("John")
            .lastName("Johnson")
            .phoneNumber("0987654321")
            .build();

    private static final Room TEST_ROOM = Room.builder()
            .number(111)
            .price(1000)
            .roomType(RoomType.APARTMENTS)
            .totalPlaces(6)
            .status(Status.AVAILABLE)
            .build();

    private static final Room TEST_ROOM_UPDATED = Room.builder()
            .id(1L)
            .number(111)
            .price(1000)
            .roomType(RoomType.PRESIDENT_SUITE)
            .totalPlaces(6)
            .status(Status.BOOKED)
            .build();

    private static final Room TEST_INVALID_ROOM = Room.builder()
            .id(1L)
            .roomType(RoomType.PRESIDENT_SUITE)
            .totalPlaces(6)
            .status(Status.BOOKED)
            .build();

    @Test
    void whenCreateRoomShouldRespondCreatedTest() throws Exception {
        Room expected = TEST_ROOM;

        //when-then
        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(expected)))
                .andExpect(status().isCreated());

        List<Room> allRooms = roomDAO.findAll();
        Room actual = allRooms.get(0);

        //assert
        assertEquals(1, allRooms.size());
        assertEquals(expected.getNumber(), actual.getNumber());
        assertEquals(expected.getRoomType(), actual.getRoomType());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getTotalPlaces(), actual.getTotalPlaces());
    }

    @Test
    @SqlGroup(
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status) VALUES (1,111, 6, 1000,'APARTMENTS','AVAILABLE')")
    )
    void whenCreateExistingRoomShouldReturnConflictTest() throws Exception {
        //when-then
        Executable ex = () -> this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_ROOM)))
                //assert
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.error", is("Conflict")))
                .andExpect(jsonPath("$.message", is("Room is already exist")));

        assertThrows(NestedServletException.class, ex);
    }

    @Test
    void whenCreateInvalidRoomShouldRespondBadRequestTest() throws Exception {
        //when-then
        this.mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_INVALID_ROOM)))
                //assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetAllEmptyShouldRespondNoContentTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/rooms"))
                //assert
                .andExpect(status().isNoContent());
    }

    @Test
    @SqlGroup({
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number) VALUES ( 1,'John','Johnson','0987654321')"),
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status, renter_id) VALUES (1,111, 6, 1000,'APARTMENTS','BOOKED', 1)")}
    )
    void whenGetAllShouldRespondOkAndReturnListTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/rooms"))
                //assert
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].number", is(111)))
                .andExpect(jsonPath("$[0].totalPlaces", is(6)))
                .andExpect(jsonPath("$[0].price", is(1000)))
                .andExpect(jsonPath("$[0].roomType", is("APARTMENTS")))
                .andExpect(jsonPath("$[0].status", is("BOOKED")))
                .andExpect(jsonPath("$[0].renter", is(TEST_CLIENT.getName() + " " + TEST_CLIENT.getLastName())));

    }

    @Test
    @SqlGroup(
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status) VALUES (1,111, 6, 1000,'APARTMENTS','AVAILABLE')")
    )
    void whenGetByIdShouldRespondOkAndReturnRoomTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/rooms/" + TEST_ID))
                //assert
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is(111)))
                .andExpect(jsonPath("$.totalPlaces", is(6)))
                .andExpect(jsonPath("$.price", is(1000)))
                .andExpect(jsonPath("$.roomType", is(("APARTMENTS"))))
                .andExpect(jsonPath("$.status", is(("AVAILABLE"))));
    }

    @Test
    void whenGetByNonExistentIdShouldRespondNotFoundTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/rooms/" + TEST_ID))
                //assert
                .andExpect(status().isNotFound());
    }

    @Test
    @SqlGroup(
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status) VALUES (1,111, 6, 1000,'APARTMENTS','AVAILABLE')")
    )
    void whenUpdateShouldRespondOkAndReturnUpdatedRoomTest() throws Exception {
        Room expected = TEST_ROOM_UPDATED;

        //when-then
        this.mockMvc.perform(put("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_ROOM_UPDATED)))
                .andExpect(status().isOk());

        List<Room> allRooms = roomDAO.findAll();
        Room actual = allRooms.get(0);

        //assert
        assertEquals(1, allRooms.size());
        assertEquals(expected.getNumber(), actual.getNumber());
        assertEquals(expected.getRoomType(), actual.getRoomType());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getTotalPlaces(), actual.getTotalPlaces());
    }

    @Test
    void whenUpdateWithNonExistentIdShouldReturnConflictTest() throws Exception {
        //when-then
        Executable ex = () -> this.mockMvc.perform(put("/rooms")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_ROOM_UPDATED)))
                //assert
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.error", is("Conflict")))
                .andExpect(jsonPath("$.message", is("Room update failed")));

        assertThrows(AssertionError.class, ex);

    }

    @Test
    @SqlGroup(
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status) VALUES (1,111, 6, 1000,'APARTMENTS','AVAILABLE')")
    )
    void whenDeleteShouldRespondOkAndDeleteClientTest() throws Exception {
        //when-then
        this.mockMvc.perform(delete("/rooms/" + TEST_ID))
                //assert
                .andExpect(status().isOk());
        List<Room> rooms = roomDAO.findAll();

        assertTrue(rooms.isEmpty());
    }

    @Test
    @SqlGroup({
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status) VALUES (1,111, 6, 1000,'APARTMENTS','AVAILABLE')")}
    )
    void whenGetAllByRenterShouldRespondNotFoundTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/rooms/client/" + TEST_ID))

                //assert
                .andExpect(status().isNotFound());
    }

    @Test
    @SqlGroup({
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number) VALUES ( 1,'John','Johnson','0987654321')"),
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status, renter_id) VALUES (1,111, 6, 1000,'APARTMENTS','BOOKED', 1)")}
    )
    void whenGetAllByRenterShouldRespondOkAndReturnListTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/rooms/client/" + TEST_ID))
                //assert
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].number", is(111)))
                .andExpect(jsonPath("$[0].totalPlaces", is(6)))
                .andExpect(jsonPath("$[0].price", is(1000)))
                .andExpect(jsonPath("$[0].roomType", is("APARTMENTS")))
                .andExpect(jsonPath("$[0].status", is("BOOKED")))
                .andExpect(jsonPath("$[0].renter", is(TEST_CLIENT.getName() + " " + TEST_CLIENT.getLastName())));
    }

    @Test
    void whenGetRoomsByStatusShouldRespondNoContentTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/rooms/available/true"))
                .andExpect(status().isNoContent());
    }

    @Test
    @SqlGroup({
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number) VALUES ( 1,'John','Johnson','0987654321')"),
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status, renter_id) VALUES (1,111, 6, 1000,'APARTMENTS','BOOKED', 1)")}
    )
    void whenGetRoomsByStatusShouldRespondOkAndReturnListTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/rooms/available/false"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].number", is(111)))
                .andExpect(jsonPath("$[0].totalPlaces", is(6)))
                .andExpect(jsonPath("$[0].price", is(1000)))
                .andExpect(jsonPath("$[0].roomType", is("APARTMENTS")))
                .andExpect(jsonPath("$[0].status", is("BOOKED")))
                .andExpect(jsonPath("$[0].renter", is(TEST_CLIENT.getName() + " " + TEST_CLIENT.getLastName())));;
    }



}
