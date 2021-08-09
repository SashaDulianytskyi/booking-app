package com.app.data.dao;

import com.app.Application;
import com.app.data.dao.repository.RoomRepository;
import com.app.data.entity.Client;
import com.app.data.entity.Room;
import com.app.data.entity.enums.RoomType;
import com.app.data.entity.enums.Status;
import com.app.exception.DBException;
import com.app.exception.NotFoundException;
import org.junit.jupiter.api.Test;
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
public class RoomDaoMockTest {
    @MockBean
    RoomRepository mockRoomRepository;

    @Autowired
    RoomDAO roomDAO;

    private static final long TEST_ID = 1L;

    private static final Room TEST_ROOM = Room.builder()
            .number(111)
            .price(1000)
            .roomType(RoomType.APARTMENTS)
            .totalPlaces(6)
            .status(Status.AVAILABLE)
            .build();

    private static final Room TEST_ROOM_EXPECTED = Room.builder()
            .id(1L)
            .number(111)
            .price(1000)
            .roomType(RoomType.APARTMENTS)
            .totalPlaces(6)
            .status(Status.AVAILABLE)
            .build();

    private static final Client TEST_CLIENT = Client.builder()
            .id(TEST_ID)
            .name("John")
            .lastName("Johnson")
            .phoneNumber("0992663548")
            .build();

    private static final Status TEST_STATUS = Status.BOOKED;

    @Test
    void whenFindByIdShouldReturnRoomTest() {
        //given
        when(mockRoomRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(TEST_ROOM_EXPECTED));

        //when
        Room actual = roomDAO.findById(TEST_ID);

        //then
        assertEquals(TEST_ROOM_EXPECTED, actual);
    }

    @Test
    void whenFindByIdReturnNullShouldThrowNotFoundExceptionTest() {
        //given
        when(mockRoomRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        //when-then
        assertThrows(NotFoundException.class, () -> roomDAO.findById(TEST_ID));
    }

    @Test
    void whenSaveShouldReturnRoomWithIdTest() {
        //given
        when(mockRoomRepository.save(TEST_ROOM)).thenReturn(TEST_ROOM_EXPECTED);

        //when
        Room actual = roomDAO.save(TEST_ROOM);

        //then
        assertEquals(TEST_ROOM_EXPECTED, actual);
    }

    @Test
    void whenSaveShouldReturnDBExceptionTest() {
        //given
        when(mockRoomRepository.save(TEST_ROOM)).thenThrow(new DataIntegrityViolationException(""));

        //when-then
        assertThrows(DBException.class, () -> roomDAO.save(TEST_ROOM));
    }

    @Test
    void whenFindAllShouldReturnListOfRoomsTest() {
        //given
        when(mockRoomRepository.findAll()).thenReturn(List.of(TEST_ROOM_EXPECTED));

        //when
        List<Room> actual = roomDAO.findAll();

        //then
        assertEquals(List.of(TEST_ROOM_EXPECTED), actual);
    }

    @Test
    void whenDeleteShouldDeleteByIdTest() {
        //when
        roomDAO.delete(TEST_ID);

        //then
        verify(mockRoomRepository).deleteById(TEST_ID);
    }

    @Test
    void whenUpdateShouldReturnRoomWithIdTest() {
        //given
        when(mockRoomRepository.save(TEST_ROOM)).thenReturn(TEST_ROOM_EXPECTED);

        //when
        Room actual = roomDAO.update(TEST_ROOM);

        //then
        assertEquals(TEST_ROOM_EXPECTED, actual);
    }

    @Test
    void whenUpdateShouldReturnDBExceptionTest() {
        //given
        when(mockRoomRepository.save(TEST_ROOM)).thenThrow(new DataIntegrityViolationException(""));

        //when-then
        assertThrows(DBException.class, () -> roomDAO.update(TEST_ROOM));
    }

    @Test
    void whenFindRoomsByClientShouldReturnListOfRoomsTest() {
        //given
        when(mockRoomRepository.findRoomsByRenter(TEST_CLIENT)).thenReturn(List.of(TEST_ROOM_EXPECTED));

        //when
        List<Room> actual = roomDAO.findRoomsByClient(TEST_CLIENT);

        //then
        assertEquals(List.of(TEST_ROOM_EXPECTED),actual);
    }

    @Test
    void whenFindAllByStatusShouldReturnListOfRoomsTest(){
        //given
        when(mockRoomRepository.findAllByStatus(TEST_STATUS)).thenReturn(List.of(TEST_ROOM_EXPECTED));

        //when
        List<Room> actual = roomDAO.findAllByStatus(TEST_STATUS);

        //then
        assertEquals(List.of(TEST_ROOM_EXPECTED),actual);
    }

}
