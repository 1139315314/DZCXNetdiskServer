package com.dzcx.netdisk.api;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import com.dzcx.netdisk.Main;
import com.dzcx.netdisk.entity.Config;
import com.dzcx.netdisk.entity.FileBean;
import com.dzcx.netdisk.entity.MP4Info;
import com.dzcx.netdisk.util.implement.IOImp;
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
        // 不是公开外链则拼接文件的路径
        if (config.getPublicFile() == null || !path.startsWith(config.getPublicFile())) {
            path = Main.root + path;
        }
        List<FileBean> list = new ArrayList<>();
        FileBean file;
        File[] files = new File(path).listFiles();
        if (files == null) return "null";
        // 遍历所有文件夹
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
        for (int i = 0, l = files.length; i < l; i++) {
            String name, format = "unknown";
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
        return gson.toJson(list);
    }

    @Override
    public String getText(JsonElement value) {
        return new IOImp().fileToString(new File(Main.root + value.getAsString()), "UTF-8");
    }

    @Override
    public String getMP4Info(JsonElement value) {
        MP4Info info = new MP4Info();

        File file = new File(Main.root + SEP + value.getAsString());
        Metadata metadata = null;
        try {
            // 获取视频文件
            metadata = ImageMetadataReader.readMetadata(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Directory dir : metadata.getDirectories()) {
            if (dir == null) continue;
            for (Tag tag : dir.getTags()) {
                String tagName = tag.getTagName();
                String desc = tag.getDescription();
                if ("Width".equals(tagName)) {
                    info.setWidth(desc.replaceAll(" pixels", ""));
                } else if ("Height".equals(tagName)) {
                    info.setHeight(desc.replaceAll(" pixels", ""));
                } else if ("Rotation".equals(tagName)) {
                    info.setDeg(Integer.valueOf(desc));
                }
            }
        }
        return gson.toJson(info);
    }
}
