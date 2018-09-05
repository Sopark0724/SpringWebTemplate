package com.web.template.common.application.data;

import lombok.Value;
import org.springframework.util.StringUtils;

@Value
public class PageListCommand {
    private int page;

    private int offset;

    private String properties;

    private String direction;

    public int getCurrentPoint(){
        return this.page * offset;
    }

    public boolean isSort(){
        return !StringUtils.isEmpty(this.properties) && !StringUtils.isEmpty(this.direction);
    }

}
