package com.web.template.attchments.domain;

import com.web.template.constants.TemplateConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.aspectj.util.FileUtil;
import org.hibernate.annotations.Type;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.File;
import java.io.IOException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Type(type = "text")
    private String fakename;

    @Type(type = "text")
    private String filename;

    @Type(type = "text")
    private String filepath;

    public Attachments updateFakeName() {
        this.fakename = new BCryptPasswordEncoder().encode(filename + System.currentTimeMillis());
        this.fakename = Base64.encodeBase64URLSafeString(this.fakename.getBytes());
        return this;
    }

    public static Attachments convert(MultipartFile multipartFile) throws IOException {
        Attachments attachments = new Attachments();
        attachments.filename = multipartFile.getOriginalFilename();
        attachments.filepath = (TemplateConstants.DEFAULT_ATTCHMENTS_SAVE_PATH + "/" + attachments.updateFakeName().fakename).replaceAll("//", "/");

        multipartFile.transferTo(new File(attachments.filepath));
        return attachments;
    }

    public byte[] readContent() throws IOException {
        return FileUtil.readAsByteArray(new File(this.filepath));
    }

}
