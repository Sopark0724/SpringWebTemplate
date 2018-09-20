package com.web.template.attchments.domain.dto;

import com.web.template.constants.TemplateConstants;
import lombok.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AttachmentsDto {

    private Long id;
    private String fakename;
    private String filename;
    private String filepath;

    public AttachmentsDto updateFakeName() {
        this.fakename = new BCryptPasswordEncoder().encode(filename + System.currentTimeMillis());
        this.fakename = Base64.encodeBase64URLSafeString(this.fakename.getBytes());
        return this;
    }

    public static AttachmentsDto convert(MultipartFile multipartFile) throws IOException {
        AttachmentsDto attachments = new AttachmentsDto();
        attachments.filename = multipartFile.getOriginalFilename();
        attachments.filepath = (TemplateConstants.DEFAULT_ATTCHMENTS_SAVE_PATH + "/" + attachments.updateFakeName().fakename).replaceAll("//", "/");

        multipartFile.transferTo(new File(attachments.filepath));
        return attachments;
    }

    public byte[] readContent() throws IOException {
        File f = new File(this.filepath);
        return Files.readAllBytes(f.toPath());
    }

}
