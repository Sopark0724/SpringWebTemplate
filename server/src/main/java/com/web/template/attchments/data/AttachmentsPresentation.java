package com.web.template.attchments.data;

import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class AttachmentsPresentation {

    private long id;
    private String filename;
    private String fakename;

    public static AttachmentsPresentation convertFrom(Object from) {
        return new ModelMapper().map(from, AttachmentsPresentation.class);
    }
}
