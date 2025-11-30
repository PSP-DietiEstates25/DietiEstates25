package com.dietiestates.resource_server.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.dietiestates.resource_server.utils.PageUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageUtilsTest {

    @Test
    @DisplayName("toPage: prima pagina piena")
    void toPage_firstFullPage() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        PageRequest pageable = PageRequest.of(0, 5); // offset = 0

        Page<Integer> page = PageUtils.toPage(list, pageable);

        assertEquals(5, page.getContent().size());
        assertEquals(List.of(1, 2, 3, 4, 5), page.getContent());
        assertEquals(0, page.getNumber());
        assertEquals(5, page.getSize());
        assertEquals(10, page.getTotalElements());
    }

    @Test
    @DisplayName("toPage: ultima pagina parziale")
    void toPage_lastPartialPage() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        PageRequest pageable = PageRequest.of(2, 5); // offset = 10

        Page<Integer> page = PageUtils.toPage(list, pageable);

        assertEquals(List.of(11, 12), page.getContent());
        assertEquals(2, page.getNumber());
        assertEquals(5, page.getSize());
        assertEquals(12, page.getTotalElements());
    }

    @Test
    @DisplayName("toPage: pagina fuori range → page vuota ma totalElements corretto")
    void toPage_pageOutOfRange_returnsEmpty() {
        List<Integer> list = List.of(1, 2, 3);
        PageRequest pageable = PageRequest.of(2, 2); // offset = 4 ≥ size=3

        Page<Integer> page = PageUtils.toPage(list, pageable);

        assertTrue(page.getContent().isEmpty());
        assertEquals(3, page.getTotalElements());
    }

    @Test
    @DisplayName("toPage: lista vuota → sempre pagina vuota")
    void toPage_emptyList_returnsEmptyPage() {
        List<Integer> list = List.of();
        PageRequest pageable = PageRequest.of(0, 5);

        Page<Integer> page = PageUtils.toPage(list, pageable);

        assertTrue(page.isEmpty());
        assertEquals(0, page.getTotalElements());
    }
}
