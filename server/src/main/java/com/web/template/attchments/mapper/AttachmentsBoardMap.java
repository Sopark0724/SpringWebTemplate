package com.web.template.attchments.mapper;

import com.web.template.attchments.domain.Attachments;
import com.web.template.board.domain.Board;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentsBoardMap {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Board board;

    @ManyToOne
    private Attachments attachment;
}
