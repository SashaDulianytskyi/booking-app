package com.app.web.validation;

import com.app.data.entity.Room;
import com.app.exception.ValidationException;
import org.springframework.util.StringUtils;

public class RoomValidator {
    private static final String EMPTY_PROPERTY_EXCEPTION_MESSAGE = "Room field parameter '%s' must be provided";

    public static void validate(Room room){
        validateNotEmptyProperty(room.getNumber(), "number");
        validateNotEmptyProperty(room.getTotalPlaces(), "totalPlaces");
        validateNotEmptyProperty(room.getRoomType(),"roomType");
        validateNotEmptyProperty(room.getPrice(),"price");
        validateNotEmptyProperty(room.getStatus(), "status");
    }

    private static void validateNotEmptyProperty(Object value, String propertyName) {
        if (value == null || StringUtils.isEmpty(value)) {
            throw new ValidationException(String.format(EMPTY_PROPERTY_EXCEPTION_MESSAGE, propertyName));
        }
    }
}
