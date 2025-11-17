package com.dietiestates.resource_server.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PageUtils {

    public static <T> Page<T> toPage(List<T> list, Pageable pageable) {
        Long start = pageable.getOffset();
        Long end = Math.min(start + pageable.getPageSize(), list.size());

        if(start >= list.size())
            return new PageImpl<>(List.of(), pageable, list.size());

        List<T> pageList = list.subList(start.intValue(), end.intValue());

        return new PageImpl<>(pageList, pageable, list.size());
    }
}
