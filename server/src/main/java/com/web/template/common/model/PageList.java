package com.web.template.common.model;

import lombok.Data;

import java.util.List;

@Data
public class PageList<T> {

    List<T> data;
    private int offset;
    private int currnetPage;
    private int totalElements;

    public int getTotalPages() {
        return totalElements / offset + (totalElements % offset != 0 ? 1 : 0);
    }
}
