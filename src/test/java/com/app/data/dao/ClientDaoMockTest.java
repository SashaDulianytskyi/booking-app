package com.app.data.dao;

import com.app.Application;
import com.app.data.dao.repository.ClientRepository;
import com.app.data.entity.Client;
import com.app.exception.DBException;
import com.app.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class ClientDaoMockTest {
    private static final long TEST_ID = 1L;

    private static final Client TEST_CLIENT = Client.builder()
            .name("John")
            .lastName("Johnson")
            .phoneNumber("0992663548")
            .build();

    private static final Client TEST_CLIENT_EXPECTED = Client.builder()
            .id(TEST_ID)
            .name("John")
            .lastName("Johnson")
            .phoneNumber("0992663548")
            .build();

    @Autowired
    ClientDAO clientDAO;

    @MockBean
    ClientRepository mockClientRepository;

    @Test
    void whenSaveClientShouldReturnClientWithIdTest() throws Exception {
        //given
        when(mockClientRepository.save(TEST_CLIENT)).thenReturn(TEST_CLIENT_EXPECTED);

        //when
        Client actual = clientDAO.save(TEST_CLIENT);

        //then
        assertEquals(TEST_CLIENT_EXPECTED, actual);
    }

    @Test
    void whenSaveShouldThrowDBExceptionTest() {
        //given
        when(mockClientRepository.save(TEST_CLIENT)).thenThrow(new DataIntegrityViolationException(""));

        //when
        Executable executable = () -> clientDAO.save(TEST_CLIENT);

        //then
        assertThrows(DBException.class, executable);
    }

    @Test
    void whenFindByIdShouldReturnClientTest() {
        //given
        when(mockClientRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(TEST_CLIENT_EXPECTED));

        //when
        Client actual = clientDAO.findById(TEST_ID);

        //then
        assertEquals(TEST_CLIENT_EXPECTED, actual);
    }

    @Test
    void whenFindByIdReturnNullShouldThrowNotFoundExceptionTest() {
        //given
        when(mockClientRepository.findById(TEST_ID)).thenReturn(Optional.empty());
        //when
        Executable executable = () -> clientDAO.findById(TEST_ID);

        //then
        assertThrows(NotFoundException.class, executable);
    }

    @Test
    void whenFindAllShouldReturnListOfClientsTest() {
        //given
        when(mockClientRepository.findAll()).thenReturn(List.of(TEST_CLIENT_EXPECTED));

        //when
        List<Client> actual = clientDAO.findAll();

        //then
        assertEquals(List.of(TEST_CLIENT_EXPECTED), actual);
    }

    @Test
    void whenDeleteShouldDeleteByIdTest() {
        //when
        clientDAO.delete(TEST_ID);

        //then
        verify(mockClientRepository).deleteById(TEST_ID);
    }

    @Test
    void whenUpdateClientShouldReturnClientWithIdTest() throws Exception {
        //given
        when(mockClientRepository.save(TEST_CLIENT)).thenReturn(TEST_CLIENT_EXPECTED);

        //when
        Client actual = clientDAO.update(TEST_CLIENT);

        //then
        assertEquals(TEST_CLIENT_EXPECTED, actual);
    }

    @Test
    void whenUpdateShouldThrowDBExceptionTest() {
        //given
        when(mockClientRepository.save(TEST_CLIENT)).thenThrow(new DataIntegrityViolationException(""));

        //when
        Executable executable = () -> clientDAO.update(TEST_CLIENT);

        //then
        assertThrows(DBException.class, executable);
    }
}
