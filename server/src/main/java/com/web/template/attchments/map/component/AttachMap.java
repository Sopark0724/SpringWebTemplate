package com.web.template.attchments.map.component;

import com.web.template.attchments.domain.dto.AttachmentsDto;
import com.web.template.attchments.type.AttachmentsType;

public interface AttachMap {
    void map(AttachmentsDto attachments, Long id);

    AttachmentsType attachmentType();
}
