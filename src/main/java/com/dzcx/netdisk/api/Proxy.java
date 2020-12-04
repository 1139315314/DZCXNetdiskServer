package com.dzcx.netdisk.api;

import com.dzcx.netdisk.Main;
import com.google.gson.JsonElement;
import org.slf4j.Logger;

public class Proxy implements API {

    private Logger log;
    private APIImpl api;

    public Proxy() {
        this.log = Main.log;
        this.api = new APIImpl();
    }


    @Override
    public String getConfig() {
        log.info("获取服务器配置。。。");
        return api.getConfig();
    }

    @Override
    public String getFileList(JsonElement value) {
        log.info("获取服务器文件列表。。。");
        return api.getFileList(value);
    }

    @Override
    public String getText(JsonElement value) {
        log.info("获取文本信息。。。");
        return api.getText(value);
    }

    @Override
    public String getMP4Info(JsonElement value) {
        log.info("获取视频信息。。。");
        return api.getMP4Info(value);
    }
}
