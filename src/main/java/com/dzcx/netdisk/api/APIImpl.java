package com.dzcx.netdisk.api;

import com.dzcx.netdisk.Main;
import com.dzcx.netdisk.entity.Config;
import com.dzcx.netdisk.entity.FileBean;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIImpl implements API {

    private static final String SEP = File.separator;

    private Gson gson = new Gson();
    private JsonParser jp = new JsonParser();
    private Config config = Main.config;

    @Override
    public String getConfig() {
        Map<String, Object> result = new HashMap<>();
        result.put("compressImg", String.valueOf(config.isCompressImg()));
        result.put("photo", SEP + config.getPhoto());
        result.put("navList", config.getNavList());
        if (config.getPublicFile() != null)
            result.put("publicFile", config.getPublicFile());
        return gson.toJson(result);
    }

    @Override
    public String getFileList(JsonElement value) {
        String path = value.getAsString();
        if (config.getPublicFile() == null || !path.startsWith(config.getPublicFile())) {
            path = Main.root + path;
        }
        List<FileBean> list = new ArrayList<>();
        FileBean file;
        File[] files = new File(path).listFiles();
        if (files == null) return "null";
        // 文件夹
        for (int i = 0, l = files.length; i < l; i++) {
            if (files[i].isHidden()) continue;
            if (!files[i].isFile()) {
                file = new FileBean();
                file.setName("folder." + files[i].getName());
                file.setDate(files[i].lastModified());
                list.add(file);
            }
        }
        // 文件
        String name, format = "unknown";
        for (int i = 0, l = files.length; i < l; i++) {
            if (files[i].isHidden()) continue;
            if (files[i].isFile()) {
                name = files[i].getName();
                format = name.lastIndexOf(".") != -1 ? name.substring(name.lastIndexOf(".") + 1) : format;
                file = new FileBean();
                file.setName(format + "." + files[i].getName());
                file.setDate(files[i].lastModified());
                file.setSize(files[i].length());
                list.add(file);
            }
        }
        return gson.toJson(list).toString();
    }
}
