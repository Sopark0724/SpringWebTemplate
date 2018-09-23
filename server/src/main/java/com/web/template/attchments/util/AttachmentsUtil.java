package com.web.template.attchments.util;

import com.web.template.constants.TemplateConstants;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

public class AttachmentsUtil {

    public static LinkedHashMap convert(MultipartFile multipartFile) throws IOException {
        LinkedHashMap<String, Object> attachments = new LinkedHashMap<>();

        String filename = multipartFile.getOriginalFilename();
        String fakename = getFakeName(filename);

        String filepath = (TemplateConstants.DEFAULT_ATTCHMENTS_SAVE_PATH + "/" + fakename).replaceAll("//", "/");


        attachments.put("filename",  filename);
        attachments.put("filepath",  filepath);
        attachments.put("fakename", fakename);

        multipartFile.transferTo(new File(filepath));
        return attachments;
    }

    public static String getFakeName(String filename) {

        String fakename = new BCryptPasswordEncoder().encode(filename + System.currentTimeMillis());
        return Base64.encodeBase64URLSafeString(fakename.getBytes());
    }

    public static byte[] readContent(Map map ) throws IOException {
        String filepath = (String) map.get("filepath");
        File f = new File(filepath);
        return Files.readAllBytes(f.toPath());
    }

}
