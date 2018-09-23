package com.web.template.attchments.map.component;

import com.web.template.attchments.util.AttachmentsUtil;
import com.web.template.attchments.type.AttachmentsType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AttachMapManager {

    private final @NonNull
    List<AttachMap> attachmentsMapInterfaces;

    private Map<AttachmentsType, AttachMap> attachmentsMapperMap = new HashMap<>();

    @PostConstruct
    private void initMap() {
        this.attachmentsMapInterfaces.forEach(mapper -> {
            try {
                this.attachmentsMapperMap.put(mapper.attachmentType(), mapper);
            } catch (Exception e) {
                log.error("component map init error : " + mapper.getClass().getName());
            }
        });
    }

    public void map(@NonNull AttachmentsType attachmentsType, @NonNull LinkedHashMap attachments, @NonNull Long id) {
        AttachMap mapper = this.attachmentsMapperMap.get(attachmentsType);
        if (mapper == null) {
            throw new NullPointerException();
        }
        mapper.map(attachments, id);
    }

}
