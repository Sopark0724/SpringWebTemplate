package com.web.tempalte.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConfigValue {

    @Value("${attchments.path:attchments/}")
    private String attchmentsPath;

    @PostConstruct
    private void initConstants() {
        TemplateConstants.DEFAULT_ATTCHMENTS_SAVE_PATH = this.attchmentsPath;
    }
}
