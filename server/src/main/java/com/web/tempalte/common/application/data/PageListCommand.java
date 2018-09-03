package com.web.tempalte.common.application.data;

import lombok.Value;

@Value
public class PageListCommand {
    private int page;

    private int offset;

    private String properties;

    private String direction;
}
