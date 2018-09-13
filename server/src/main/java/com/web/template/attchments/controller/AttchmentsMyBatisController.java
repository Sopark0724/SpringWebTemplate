package com.web.template.attchments.controller;

import com.web.template.attchments.data.AttachmentsPresentation;
import com.web.template.attchments.service.AttachmentsService;
import com.web.template.attchments.type.AttachmentsType;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/v2/attachments")
public class AttchmentsMyBatisController {

    @Autowired
    @Qualifier("attachmentsMyBatisServiceImpl")
    private AttachmentsService attachmentsService;

    // TODO : multipart-form 파라미터 처리해야됨.
    @ApiOperation(value = "첨부파일 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attchmentsType", value = "첨부파일 앱 구분 (BOARD 등)", dataType = "String", paramType = "path", defaultValue = ""),
            @ApiImplicitParam(name = "id", value = "앱(게시판 등) id", required = true, dataType = "Long", paramType = "path", defaultValue = "")
    })
    @PostMapping("/upload/{attchmentsType}/{id}")
    public List<AttachmentsPresentation> uploadFile(@PathVariable(value = "attchmentsType") AttachmentsType attachmentsType, @PathVariable("id") Long id, HttpServletRequest req) {
        return this.attachmentsService.uploadFiles(attachmentsType, id, MultipartHttpServletRequest.class.cast(req));
    }

    @ApiOperation(value = "첨부파일 다운로드")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fakename", value = "서버에서 받은 첨부파일 경로", dataType = "String", paramType = "path", defaultValue = "")
    })
    @GetMapping("/download/{fakename}")
    public HttpEntity<byte[]> downloadFile(@PathVariable String fakename) {
        return this.attachmentsService.downloadFile(fakename);
    }
}
