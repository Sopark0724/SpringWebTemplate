package com.web.template.attchments.service;

import com.web.template.attchments.data.AttachmentsPresentation;
import com.web.template.attchments.domain.dao.AttachmentsBoardMapDao;
import com.web.template.attchments.domain.dao.AttachmentsDao;
import com.web.template.attchments.domain.dto.AttachmentsBoardMapDto;
import com.web.template.attchments.domain.dto.AttachmentsDto;
import com.web.template.attchments.type.AttachmentsType;
import com.web.template.board.domain.dao.BoardDao;
import com.web.template.board.domain.dto.BoardDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentsMyBatisServiceImpl implements AttachmentsService {

    private final @NonNull
    BoardDao boardDao;

    private final @NonNull
    AttachmentsDao attachmentsDao;

    private final @NonNull
    AttachmentsBoardMapDao attachmentsBoardMapDao;

    public List<AttachmentsPresentation> uploadFiles(AttachmentsType attachmentsType, Long id, MultipartHttpServletRequest mReq) {

        if (attachmentsType == null || !attachmentsType.equals(AttachmentsType.BOARD)) {
            log.error("unsuppoted attachments type : " + attachmentsType);
            throw new UnsupportedMediaTypeStatusException(attachmentsType.name());
        }

        List<MultipartFile> mfiles = new ArrayList<>();
        List<AttachmentsDto> attachmentsList = new ArrayList<>();

        BoardDto boardDto = this.boardDao.findById(id);

        for (Map.Entry<String, List<MultipartFile>> entry : mReq.getMultiFileMap().entrySet()) {
            mfiles.addAll(entry.getValue());
        }

        assert mfiles.size() > 0;

        for (MultipartFile mfile : mfiles) {
            try {
                attachmentsList.add(AttachmentsDto.convert(mfile));
            } catch (IOException e) {
                log.error("attachments convert error : Io Exception " + e.getMessage());
            }
        }
        List<AttachmentsPresentation> resultList = new ArrayList<>();
        attachmentsList = this.attachmentsDao.saveAll(attachmentsList);

        for (AttachmentsDto attachmentsDto : attachmentsList) {
            this.attachmentsBoardMapDao.save(AttachmentsBoardMapDto.mapping(boardDto, attachmentsDto));
            resultList.add(AttachmentsPresentation.convertFrom(attachmentsDto));
        }
        return resultList;
    }

    public HttpEntity<byte[]> downloadFile(String fakename) {
        AttachmentsDto attachments = this.attachmentsDao.findByFakename(fakename);

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
