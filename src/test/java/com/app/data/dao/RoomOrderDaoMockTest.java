package com.app.data.dao;

import com.app.Application;
import com.app.data.dao.repository.RoomOrderRepository;
import com.app.data.entity.Client;
import com.app.data.entity.Room;
import com.app.data.entity.RoomOrder;
import com.app.data.entity.enums.PaymentMethod;
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

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class RoomOrderDaoMockTest {
    @Autowired
    RoomOrderDAO roomOrderDAO;

    @MockBean
    RoomOrderRepository mockRoomOrderRepository;

    private static final long TEST_ID = 1L;

    private static final Date TEST_CHECK_IN_DATE = new Date(System.currentTimeMillis());
    private static final Date TEST_CHECK_OUT_DATE = new Date(System.currentTimeMillis() + 1L);


    private static final Client TEST_CLIENT = Client.builder()
            .id(TEST_ID)
            .name("John")
            .lastName("Johnson")
            .phoneNumber("0992663548")
            .build();

    private static final Room TEST_ROOM = Room.builder()
            .id(1L)
            .number(111)
            .price(1000)
            .roomType(RoomType.APARTMENTS)
            .totalPlaces(6)
            .status(Status.AVAILABLE)
            .build();

    private static final RoomOrder TEST_ROOM_ORDER = RoomOrder.builder()
            .paymentMethod(PaymentMethod.CASH)
            .cInDate(TEST_CHECK_IN_DATE)
            .cOutDate(TEST_CHECK_OUT_DATE)
            .room(TEST_ROOM)
            .client(TEST_CLIENT)
            .build();

    private static final RoomOrder TEST_ROOM_ORDER_EXPECTED = RoomOrder.builder()
            .id(TEST_ID)
            .paymentMethod(PaymentMethod.CASH)
            .cInDate(TEST_CHECK_IN_DATE)
            .cOutDate(TEST_CHECK_OUT_DATE)
            .room(TEST_ROOM)
            .client(TEST_CLIENT)
            .build();

    @Test
    void whenFindByIdShouldReturnOrderTest() {
        //given
        when(mockRoomOrderRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(TEST_ROOM_ORDER_EXPECTED));

        //when
        RoomOrder actual = roomOrderDAO.findById(TEST_ID);

        //then
        assertEquals(TEST_ROOM_ORDER_EXPECTED, actual);
    }

    @Test
    void whenFindByIdReturnNullShouldThrowNotFoundExceptionTest() {
        //given
        when(mockRoomOrderRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        //when-then
        assertThrows(NotFoundException.class, () -> roomOrderDAO.findById(TEST_ID));
    }

    @Test
    void whenFindByClientShouldReturnListOfOrdersTest() {
        //given
        when(mockRoomOrderRepository.findAllByClient(TEST_CLIENT)).thenReturn(List.of(TEST_ROOM_ORDER_EXPECTED));

        //when
        List<RoomOrder> actual = roomOrderDAO.findByClient(TEST_CLIENT);

        //then
        assertEquals(List.of(TEST_ROOM_ORDER_EXPECTED), actual);
    }

    @Test
    void whenFindAllShouldReturnListOfOrdersTest() {
        //given
        when(mockRoomOrderRepository.findAll()).thenReturn(List.of(TEST_ROOM_ORDER_EXPECTED));

        //when
        List<RoomOrder> actual = roomOrderDAO.findAll();

        //then
        assertEquals(List.of(TEST_ROOM_ORDER_EXPECTED), actual);
    }

    @Test
    void whenSaveShouldReturnRoomOrderWithIdTest() {
        //given
        when(mockRoomOrderRepository.save(TEST_ROOM_ORDER)).thenReturn(TEST_ROOM_ORDER_EXPECTED);

        //when
        RoomOrder actual = roomOrderDAO.save(TEST_ROOM_ORDER);

        //then
        assertEquals(TEST_ROOM_ORDER_EXPECTED, actual);
    }

    @Test
    void whenSaveShouldThrowDBExceptionTest() {
        //given
        when(mockRoomOrderRepository.save(TEST_ROOM_ORDER)).thenThrow(new DataIntegrityViolationException(""));

        //when-then
        assertThrows(DBException.class, () -> roomOrderDAO.save(TEST_ROOM_ORDER));
    }

    @Test
    void whenDeleteShouldDeleteByIdTest() {
        //when
        roomOrderDAO.delete(TEST_ID);

        //then
        verify(mockRoomOrderRepository).deleteById(TEST_ID);
    }

    @Test
    void whenUpdateShouldReturnRoomOrderWithIdTest() {
        //given
        when(mockRoomOrderRepository.save(TEST_ROOM_ORDER_EXPECTED)).thenReturn(TEST_ROOM_ORDER_EXPECTED);

        //when
        RoomOrder actual = roomOrderDAO.update(TEST_ROOM_ORDER_EXPECTED);

        //then
        assertEquals(TEST_ROOM_ORDER_EXPECTED, actual);
    }

    @Test
    void whenUpdateShouldThrowDBExceptionTest() {
        //given
        when(mockRoomOrderRepository.save(TEST_ROOM_ORDER_EXPECTED)).thenThrow(new DataIntegrityViolationException(""));

        //when-then
        assertThrows(DBException.class, () -> roomOrderDAO.update(TEST_ROOM_ORDER_EXPECTED));
    }


}
