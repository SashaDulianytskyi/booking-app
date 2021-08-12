package com.app.web.controller.integration;

import com.app.data.dao.RoomOrderDAO;
import com.app.data.entity.Client;
import com.app.data.entity.Room;
import com.app.data.entity.RoomOrder;
import com.app.data.entity.enums.PaymentMethod;
import com.app.data.entity.enums.RoomType;
import com.app.data.entity.enums.Status;
import com.app.web.controller.ControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderControllerIntegrationTest extends ControllerIntegrationTest {

    @Autowired
    RoomOrderDAO orderDAO;

    private static final Long TEST_ID = 1L;

    private static final Client TEST_CLIENT = Client.builder()
            .id(1L)
            .name("John")
            .lastName("Johnson")
            .phoneNumber("0987654321")
            .build();

    private static final Room TEST_ROOM = Room.builder()
            .id(1L)
            .number(111)
            .price(1000)
            .roomType(RoomType.APARTMENTS)
            .totalPlaces(6)
            .status(Status.BOOKED)
            .build();

    private static final RoomOrder TEST_ORDER = RoomOrder.builder()
            .paymentMethod(PaymentMethod.CREDIT_CARD)
            .build();

    private static final RoomOrder INVALID_ORDER_TEST = RoomOrder.builder().build();

    private static final RoomOrder TEST_ORDER_UPDATED = RoomOrder.builder()
            .id(1L)
            .paymentMethod(PaymentMethod.CASH)
            .build();

    @Test
    @SqlGroup({
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number) " +
                    "VALUES ( 1,'John','Johnson','0987654321')"),
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status, renter_id) " +
                    "VALUES (1,111, 6, 1000,'APARTMENTS','BOOKED', 1)")

    })
    void whenCreateOrderShouldRespondCreatedAndReturnOrderTest() throws Exception {
        //when-then
        this.mockMvc.perform(post(String.format("/orders/client/%d/room/%d/days/%d", TEST_ID, TEST_ID, 5))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_ORDER)))
                .andExpect(status().isCreated());

        List<RoomOrder> allOrders = orderDAO.findAll();
        RoomOrder actual = allOrders.get(0);

        //assert
        assertEquals(1, allOrders.size());
        assertEquals(TEST_ORDER.getPaymentMethod(), actual.getPaymentMethod());
        assertNotNull(actual.getCInDate());
        assertNotNull(actual.getCOutDate());
        assertEquals(TEST_CLIENT, actual.getClient());
        assertEquals(TEST_ROOM, actual.getRoom());

        assertTrue(Objects.nonNull(actual.getId()) && actual.getId() > 0);
    }

    @Test
    @SqlGroup({
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number) " +
                    "VALUES ( 1,'John','Johnson','0987654321')"),
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status, renter_id) " +
                    "VALUES (1,111, 6, 1000,'APARTMENTS','BOOKED', 1)"),
            @Sql(statements = "INSERT INTO room_order (id, payment_method, check_in_date, check_out_date, client_id, room_id) " +
                    "VALUES (1, 'CREDIT_CARD', DATE '2021-12-17', DATE '2021-12-19',1,1)")
    })
    void whenCreateExistingOrderShouldReturnConflictTest() throws Exception {
        //when-then
        this.mockMvc.perform(post(String.format("/orders/client/%d/room/%d/days/%d", TEST_ID, TEST_ID, 5))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_ORDER)))
                //assert
                .andExpect(status().isConflict());
    }

    @Test
    @SqlGroup({
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number) " +
                    "VALUES ( 1,'John','Johnson','0987654321')"),
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status, renter_id) " +
                    "VALUES (1,111, 6, 1000,'APARTMENTS','BOOKED', 1)")

    })
    void whenCreateInvalidOrderShouldRespondInvalidTest() throws Exception {
        this.mockMvc.perform(post(String.format("/orders/client/%d/room/%d/days/%d", TEST_ID, TEST_ID, 5))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(INVALID_ORDER_TEST)))
                //assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateOrderByNonExistentClientOrRoomShouldRespondNotFoundTest() throws Exception {
        //when-then
        this.mockMvc.perform(post(String.format("/orders/client/%d/room/%d/days/%d", TEST_ID, TEST_ID, 5))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_ORDER)))
                //assert
                .andExpect(status().isNotFound());

    }

    @Test
    void whenGetAllShouldRespondNoContentTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/orders"))
                //assert
                .andExpect(status().isNoContent());
    }

    @Test
    @SqlGroup({
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number) " +
                    "VALUES ( 1,'John','Johnson','0987654321')"),
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status, renter_id) " +
                    "VALUES (1,111, 6, 1000,'APARTMENTS','BOOKED', 1)"),
            @Sql(statements = "INSERT INTO room_order (id, payment_method, check_in_date, check_out_date, client_id, room_id) " +
                    "VALUES (1, 'CREDIT_CARD', DATE '2021-12-17', '2021-12-19',1,1)")
    })
    void whenGetAllShouldRespondOkAndReturnListTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/orders"))
                //assert
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].paymentMethod", is("CREDIT_CARD")))
                .andExpect(jsonPath("$[0].checkInDate", is("2021-12-17")))
                .andExpect(jsonPath("$[0].checkOutDate", is("2021-12-19")))
                .andExpect(jsonPath("$[0].client", is("John Johnson")))
                .andExpect(jsonPath("$[0].room", is("111")));
    }

    @Test
    @SqlGroup({
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number)" +
                    "VALUES ( 1,'John','Johnson','0987654321')"),
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status, renter_id) " +
                    "VALUES (1,111, 6, 1000,'APARTMENTS','BOOKED', 1)"),
            @Sql(statements = "INSERT INTO room_order (id, payment_method, check_in_date, check_out_date, client_id, room_id) " +
                    "VALUES (1, 'CREDIT_CARD', DATE '2021-12-17', '2021-12-19',1,1)")
    })
    void whenGetByIdShouldRespondOkAndReturnRoomTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/orders/1"))
                //assert
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentMethod", is("CREDIT_CARD")))
                .andExpect(jsonPath("$.checkInDate", is("2021-12-17")))
                .andExpect(jsonPath("$.checkOutDate", is("2021-12-19")))
                .andExpect(jsonPath("$.client", is("John Johnson")))
                .andExpect(jsonPath("$.room", is("111")));
    }

    @Test
    void whenGetByNonExistentIdShouldRespondNotFoundTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/orders/" + TEST_ID))
                //assert
                .andExpect(status().isNotFound());
    }

    @Test
    @SqlGroup({
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number)" +
                    "VALUES ( 1,'John','Johnson','0987654321')"),
            @Sql(statements = "INSERT INTO room (id, number, total_places, price, type, status, renter_id) " +
                    "VALUES (1,111, 6, 1000,'APARTMENTS','BOOKED', 1)"),
            @Sql(statements = "INSERT INTO room_order (id, payment_method, check_in_date, check_out_date, client_id, room_id) " +
                    "VALUES (1, 'CREDIT_CARD', DATE '2021-12-17', '2021-12-19',1,1)")
    })
    void whenGetAllByClientShouldRespondOkAndReturnListTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/orders/client/" + TEST_ID))
                //assert
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].paymentMethod", is("CREDIT_CARD")))
                .andExpect(jsonPath("$[0].checkInDate", is("2021-12-17")))
                .andExpect(jsonPath("$[0].checkOutDate", is("2021-12-19")))
                .andExpect(jsonPath("$[0].client", is("John Johnson")))
                .andExpect(jsonPath("$[0].room", is("111")));
    }




}
