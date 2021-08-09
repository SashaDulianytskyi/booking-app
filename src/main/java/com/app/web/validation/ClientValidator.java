package com.app.web.validation;

import com.app.data.entity.Client;

import com.app.exception.valid.ClientValidationException;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientValidator {
    private static final String VALID_PHONE_NUMBER_REGEX = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";

    private static final String EMPTY_PROPERTY_EXCEPTION_MESSAGE = "Client field parameter '%s' must be provided";
    private static final String REGEX_EXCEPTION_MESSAGE = "Client field parameter '%s' must match these parameters: '%s'";

    public static void validate(Client client){
        validateNotEmptyProperty(client.getName(), "name");
        validateNotEmptyProperty(client.getLastName(), "lastName");
        validateWithRegularExpression(client.getPhoneNumber(), VALID_PHONE_NUMBER_REGEX, "phoneNumber", "Incorrect phone number");
    }

    private static void validateNotEmptyProperty(Object value, String propertyName) {
        if (value == null || StringUtils.isEmpty(value)) {
            throw new ClientValidationException(String.format(EMPTY_PROPERTY_EXCEPTION_MESSAGE, propertyName));
        }
    }

    private static void validateWithRegularExpression(Object value, String regex, String propertyName, String exceptionMessage) {
        Matcher matcher = Pattern.compile(regex).matcher(String.valueOf(value));
        if (!matcher.matches()) {
            throw new ClientValidationException(String.format(REGEX_EXCEPTION_MESSAGE, propertyName, exceptionMessage));
        }
    }

}
