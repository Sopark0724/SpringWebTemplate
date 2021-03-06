package com.web.template.common.application.data;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.StringUtils;

@Value
@AllArgsConstructor
public class PageCommand {
    private int start;
    private int limit;
    private String properties;
    private String direction;

    public PageCommand() {
        this.start = 0;
        this.limit = 10;
        this.properties = null;
        this.direction = null;
    }

    public int getCurrentPoint() {
        return this.start * limit;
    }

    public boolean isSort() {
        return !StringUtils.isEmpty(this.properties) && !StringUtils.isEmpty(this.direction);
    }

    public RowBounds rowBounds() {
        return new RowBounds(this.start, this.limit);
    }

    public int getPage() {
        return start / limit;
    }
}
