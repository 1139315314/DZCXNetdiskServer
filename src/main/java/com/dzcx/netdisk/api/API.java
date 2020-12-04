package com.dzcx.netdisk.api;

import com.google.gson.JsonElement;

public interface API {

    // 获取服务器的配置
    public String getConfig();

    // 获取文件列表
    public String getFileList(JsonElement value);

    // 获取文本数据
    public String getText(JsonElement value);

    // 获取视频的数据
    public String getMP4Info(JsonElement value);
}
