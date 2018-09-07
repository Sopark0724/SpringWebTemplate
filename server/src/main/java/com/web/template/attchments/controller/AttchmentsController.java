package com.web.template.attchments.controller;

import com.web.template.attchments.data.AttachmentsPresentation;
import com.web.template.attchments.service.AttachmentsService;
import com.web.template.attchments.type.AttachmentsType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attachments")
public class AttchmentsController {
    private final @NonNull
    AttachmentsService attachmentsService;

    @PostMapping("/upload/{attchmentsType}/{id}")
    public List<AttachmentsPresentation> uploadFile(@PathVariable(value = "attchmentsType") AttachmentsType attachmentsType, @PathVariable("id") Long id, HttpServletRequest req) {
        return this.attachmentsService.uploadFiles(attachmentsType, id, MultipartHttpServletRequest.class.cast(req));
    }

    @GetMapping("/download/{fakename}")
    public HttpEntity<byte[]> downloadFile(@PathVariable String fakename) {
        return this.attachmentsService.downloadFile(fakename);
    }
}
