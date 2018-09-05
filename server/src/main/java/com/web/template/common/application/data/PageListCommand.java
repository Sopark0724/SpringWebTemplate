package com.web.template.common.application.data;

import lombok.Value;

@Value
public class PageListCommand {
    private int page;

    private int offset;

    private String properties;

    private String direction;

    public int getCurrentPoint(){
        return this.page * offset;
    }

}
