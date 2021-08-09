package com.app.web.validation;

import com.app.data.entity.RoomOrder;
import com.app.exception.valid.OrderValidationException;
import org.springframework.util.StringUtils;

public class OrderValidator {
    private static final String EMPTY_PROPERTY_EXCEPTION_MESSAGE = "Order field parameter '%s' must be provided";

    public static void validate(RoomOrder roomOrder) {
        validateNotEmptyProperty(roomOrder.getPaymentMethod(), "paymentMethod");
    }

    private static void validateNotEmptyProperty(Object value, String propertyName) {
        if (value == null || StringUtils.isEmpty(value)) {
            throw new OrderValidationException(String.format(EMPTY_PROPERTY_EXCEPTION_MESSAGE, propertyName));
        }
    }
}
