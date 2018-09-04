package com.web.tempalte.common.infra;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.web.tempalte.common.application.data.RemoteFileUploadCommand;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;

@Slf4j
public class RemoteFileUploader {

    public static boolean remoteFileUpload(RemoteFileUploadCommand command){
        File file = new File(command.getLocalFilePath());

        System.out.println("=> Connecting to " + command.getHost());

        log.info("=> Connecting to {}", command.getHost());

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

        FileInputStream in = null;

        JSch jsch = new JSch();

        try {
            session = jsch.getSession(command.getLoginId(), command.getHost(), command.getPort());
            session.setPassword(command.getPassword());

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no"); // 인증서 검사를 하지 않음
            session.setConfig(config);

            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();

            channelSftp = (ChannelSftp)channel;
            log.info("=> Connected to {}", command.getHost());

            in = new FileInputStream(file);
            in = new FileInputStream(file);

            channelSftp.cd(command.getUploadPath());
            channelSftp.put(in, file.getName());

            System.out.println("=> Uploaded : " + file.getPath() + " at " + command.getHost());
            log.info("=> Uploaded : {} at {}", file.getPath(), command.getHost());
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                in.close();
                channelSftp.exit();
                channel.disconnect();
                session.disconnect();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
