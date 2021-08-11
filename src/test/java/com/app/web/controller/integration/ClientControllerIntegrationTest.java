package com.app.web.controller.integration;

import com.app.data.dao.ClientDAO;
import com.app.data.entity.Client;
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
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ClientControllerIntegrationTest extends ControllerIntegrationTest {

    @Autowired
    private ClientDAO clientDAO;

    private static final Long TEST_ID = 1L;

    private static final Client TEST_CLIENT = Client.builder()
            .name("John")
            .lastName("Johnson")
            .phoneNumber("0987654321")
            .build();

    private static final Client TEST_CLIENT_UPDATED = Client.builder()
            .id(1L)
            .name("John")
            .lastName("Lennon")
            .phoneNumber("0987654321")
            .build();

    private static final Client TEST_INVALID_CLIENT = Client.builder()
            .id(1L)
            .name("John")
            .lastName("Johnson")
            .phoneNumber("09876")
            .build();

    @Test
    void whenCreateClientShouldRespondCreatedTest() throws Exception {
        Client expected = TEST_CLIENT;

        //when-then
        this.mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(expected)))
                .andExpect(status().isCreated());

        List<Client> allClients = clientDAO.findAll();
        Client actual = allClients.get(0);

        //assert
        assertEquals(1, allClients.size());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());

        assertTrue(Objects.nonNull(actual.getId()) && actual.getId() > 0);
    }

    @Test
    @SqlGroup(
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number) VALUES ( 1,'John','Johnson','0987654321')")
    )
    void whenCreateExistingClientShouldReturnConflictTest() throws Exception {
        //when-then
        Executable ex = () -> this.mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_CLIENT)))
                //assert
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.error", is("Conflict")))
                .andExpect(jsonPath("$.message", is("Client is already exist")));

        assertThrows(NestedServletException.class, ex);

    }

    @Test
    void whenCreateInvalidClientShouldRespondBadRequestTest() throws Exception {
        //when-then
        this.mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_INVALID_CLIENT)))
                //assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetAllEmptyShouldRespondNoContentTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                //assert
                .andExpect(status().isNoContent());
    }

    @Test
    @SqlGroup(
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number) VALUES ( 1,'John','Johnson','0987654321')")
    )
    void whenGetAllShouldRespondOkAndReturnListTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                //assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Johnson")))
                .andExpect(jsonPath("$[0].phoneNumber", is("0987654321")));
    }

    @Test
    @SqlGroup(
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number) VALUES ( 1,'John','Johnson','0987654321')")
    )
    void whenGetByIdShouldRespondOkAndReturnClientTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/clients/" + TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                //assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.lastName", is("Johnson")))
                .andExpect(jsonPath("$.phoneNumber", is("0987654321")));
    }

    @Test
    void whenGetByNonExistentIdShouldRespondNotFoundTest() throws Exception {
        //when-then
        this.mockMvc.perform(get("/clients/" + TEST_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                //assert
                .andExpect(status().isNotFound());
    }

    @Test
    @SqlGroup(
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number) VALUES ( 1,'John','Johnson','0987654321')")
    )
    void whenUpdateShouldRespondOkAndReturnUpdatedClientTest() throws Exception {
        Client expected = TEST_CLIENT_UPDATED;

        //when-then
        this.mockMvc.perform(put("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_CLIENT_UPDATED)))
                .andExpect(status().isOk());

        List<Client> allClients = clientDAO.findAll();
        Client actual = allClients.get(0);
        //assert
        assertEquals(1, allClients.size());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
    }

    @Test
    void whenUpdateWithNonExistentIdShouldReturnConflictTest() throws Exception {
        //when-then
        Executable ex = () -> this.mockMvc.perform(put("/clients")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_CLIENT_UPDATED)))
                //assert
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.error", is("Conflict")))
                .andExpect(jsonPath("$.message", is("Client update failed")));

        assertThrows(AssertionError.class, ex);

    }

    @Test
    @SqlGroup(
            @Sql(statements = "INSERT INTO client (id, name, last_name, phone_number) VALUES ( 1,'John','Johnson','0987654321')")
    )
    void whenDeleteShouldRespondOkAndDeleteClientTest() throws Exception {
        //when-then
        this.mockMvc.perform(delete("/clients/" + TEST_ID))
                //assert
                .andExpect(status().isOk());
        List<Client> clients = clientDAO.findAll();

        assertTrue(clients.isEmpty());
    }
}
