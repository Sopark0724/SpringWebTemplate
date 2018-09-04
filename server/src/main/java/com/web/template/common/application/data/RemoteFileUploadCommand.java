package com.web.template.common.application.data;

import lombok.Value;

@Value
public class RemoteFileUploadCommand {
    RemoteServerInfo remoteServerInfo;
    private String localFilePath;

    public String getLoginId() {
        return this.remoteServerInfo.getLoginId();
    }

    public String getHost() {
        return this.remoteServerInfo.getHost();
    }

    public int getPort() {
        return this.remoteServerInfo.getPort();
    }

    public String getPassword() {
        return this.remoteServerInfo.getPassword();
    }

    public String getUploadPath() {
        return this.remoteServerInfo.getUploadPath();
    }
}
