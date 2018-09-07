package com.web.template.board.application.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardAddCommand {
    private String title;

    private String contents;
}
