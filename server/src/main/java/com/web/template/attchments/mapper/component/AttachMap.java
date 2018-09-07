package com.web.template.attchments.mapper.component;

import com.web.template.attchments.domain.Attachments;
import com.web.template.attchments.type.AttachmentsType;

public interface AttachMap {
    void map(Attachments attachments, Long id);

    AttachmentsType attachmentType();
}
