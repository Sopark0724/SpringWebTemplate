package com.web.template.attchments.service;

import com.web.template.attchments.data.AttachmentsPresentation;
import com.web.template.attchments.domain.Attachments;
import com.web.template.attchments.mapper.component.AttachMapManager;
import com.web.template.attchments.repository.AttachmentsRepository;
import com.web.template.attchments.type.AttachmentsType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentsJpaServiceImpl implements AttachmentsService {

    private final @NonNull
    AttachmentsRepository attachmentsRepository;

    private final @NonNull
    AttachMapManager attachMapManager;

    public List<AttachmentsPresentation> uploadFiles(AttachmentsType attachmentsType, Long id, MultipartHttpServletRequest mReq) {
        List<MultipartFile> mfiles = new ArrayList<>();
        mReq.getMultiFileMap().entrySet().forEach(e -> mfiles.addAll(e.getValue()));
        assert mfiles.size() > 0;

        List<Attachments> attachmentsList = mfiles.stream().map(mf -> {
            try {
                return Attachments.convert(mf);
            } catch (IOException e) {
                log.error("attachments convert error : Io Exception " + e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        attachmentsList = this.attachmentsRepository.saveAll(attachmentsList);

        attachmentsList.forEach(attachments -> this.attachMapManager.map(attachmentsType, attachments, id));

        return attachmentsList.stream().map(AttachmentsPresentation::convertFrom).collect(Collectors.toList());
    }

    public HttpEntity<byte[]> downloadFile(String fakename) {
        Attachments attachments = this.attachmentsRepository.findFirstByFakename(fakename);

        if (attachments == null) {
            throw new NullPointerException();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + attachments.getFilename().replace(" ", "_"));

        byte[] content = null;
        try {
            content = attachments.readContent();
        } catch (IOException e) {
            log.error("file read error : " + attachments.getFilepath());
            throw new NullPointerException();
        }

        httpHeaders.setContentLength(content.length);

        return new HttpEntity<>(content, httpHeaders);

    }
}
