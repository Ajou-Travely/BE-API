package com.ajou.travely.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Converter
public class ScheduleOrderConverter implements AttributeConverter<List<Long>, String> {
    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        if (Objects.isNull(attribute)) {
            return "";
        }
        return attribute
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData) || dbData.isBlank()) {
            return new ArrayList();
        } else {
            return Arrays
                    .stream(dbData.split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }
    }
}
