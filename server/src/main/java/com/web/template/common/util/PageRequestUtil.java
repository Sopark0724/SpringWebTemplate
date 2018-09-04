package com.web.template.common.util;

import com.web.template.common.application.data.PageListCommand;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import static org.springframework.data.domain.Sort.Direction.fromString;

public class PageRequestUtil {

    public static PageRequest create(PageListCommand command){
        if(StringUtils.isEmpty(command.getDirection())){
           return PageRequest.of(command.getPage(), command.getOffset());
        }

        return PageRequest.of(command.getPage(), command.getOffset(), fromString(command.getDirection()), command.getProperties());
    }
}
