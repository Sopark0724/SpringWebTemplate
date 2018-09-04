package com.web.template.board.application.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardAddCommand {
    private String title;

    private String contents;
}
