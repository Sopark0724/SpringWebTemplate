package com.web.template.attchments.service;

import com.web.template.attchments.data.AttachmentsPresentation;
import com.web.template.attchments.type.AttachmentsType;
import org.springframework.http.HttpEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface AttachmentsService {
    List<AttachmentsPresentation> uploadFiles(AttachmentsType attachmentsType, Long id, MultipartHttpServletRequest mReq);

    HttpEntity<byte[]> downloadFile(String fakename);
}
