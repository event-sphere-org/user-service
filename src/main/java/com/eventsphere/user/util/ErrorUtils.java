package com.eventsphere.user.util;

import lombok.experimental.UtilityClass;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ErrorUtils {

    public Map<String, List<String>> getFieldErrors(List<FieldError> fieldErrors) {
        Map<String, List<String>> errorsMap = new HashMap<>();

        for (FieldError error : fieldErrors) {
            errorsMap.putIfAbsent(error.getField(), new ArrayList<>());
            errorsMap.get(error.getField()).add(error.getDefaultMessage());
        }

        return errorsMap;
    }
}
