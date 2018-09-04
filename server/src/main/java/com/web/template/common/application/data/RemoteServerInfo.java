package com.web.template.common.application.data;

import lombok.Value;

@Value
class RemoteServerInfo {
    private String loginId;
    private String host;
    private int port;
    private String password;
    private String uploadPath;
}
