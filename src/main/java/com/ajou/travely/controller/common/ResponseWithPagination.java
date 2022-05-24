package com.ajou.travely.controller.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ResponseWithPagination<T> {
    private final Integer page;
    private final Integer size;
    private final List<T> data;
}
