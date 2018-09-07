package com.web.template.board.application.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BoardAddCommand {
    private String title;

    private String contents;
}
