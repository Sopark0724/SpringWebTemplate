package com.web.template.attchments.map.component;

import com.web.template.attchments.util.AttachmentsUtil;
import com.web.template.attchments.type.AttachmentsType;

import java.util.LinkedHashMap;

public interface AttachMap {
    void map(LinkedHashMap attachments, Long id);

    AttachmentsType attachmentType();
}
