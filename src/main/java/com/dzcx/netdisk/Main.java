package com.dzcx.netdisk;

import com.dzcx.netdisk.entity.Config;
import com.dzcx.netdisk.listener.PublicListener;
import com.dzcx.netdisk.listener.StateListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    // 版本
    private static final String VERSION = "1.0.0";
    // 日志
    public static final Logger log = LoggerFactory.getLogger(Main.class);
    // 项目的根路径
    public static String root;
    // 读取的配置文件
    public static Config config;

    public static void main(String[] args) {
        long setupTime = System.currentTimeMillis();

        log.info("正在加载配置中···");
        config = getConfig("src/main/resources/iNetdiskServer.ini");

        // 在项目根目录下创建root目录
        root = config.getRoot();
        String projectDir = System.getProperty("user.dir");
        projectDir = projectDir.replaceAll("\\\\", "\\\\\\\\");
        root = root.replaceAll("%core%", projectDir);
        File rootFolder = new File(root);
        if (!rootFolder.exists()) rootFolder.mkdirs();

        log.info("网盘的根目录：" + root);

        // 生成默认文件夹 [{"U 盘": "U 盘"}, {"视频": "视频"}, {"其他备份": "其他备份"}]
        try {
            JsonObject jo;
            JsonArray ja = (new JsonParser()).parse(config.getNavList()).getAsJsonArray();
            for (int i = 0; i < ja.size(); i++) {
                jo = ja.get(i).getAsJsonObject();
                for (Map.Entry<String, JsonElement> item : jo.entrySet()) {
                    (new File(Main.root + File.separator + item.getValue().getAsString())).mkdirs();
                }
            }
        } catch (Exception e) {
            log.error("无法解析配置文件的默认文件夹");
            log.error(e.getMessage());
        }

        final int PUBLIC_PORT = config.getPortPublic();
        final int STATE_PORT = config.getPortState();
        final int UPLOAD_PORT = config.getPortUpload();
        final int DOWNLOAD_PORT = config.getPortDownload();
        log.info("通用请求端口：" + PUBLIC_PORT);
        log.info("状态请求端口：" + STATE_PORT);
        log.info("上传文件端口：" + UPLOAD_PORT);
        log.info("下载文件端口：" + DOWNLOAD_PORT);

        // 启动监听核心
        log.info("正在启动服务..");
        // 服务器状态监听
        new StateListener(STATE_PORT).start();
        // 公共请求监听
        new PublicListener(PUBLIC_PORT).start();






        long finshTime = System.currentTimeMillis();
        log.info("启动完成，网盘版本为：" + VERSION + "启动耗时：" + (finshTime - setupTime) + "ms");

        Scanner scanner = new Scanner(System.in);
        String command;
        while (true) {
            command = scanner.nextLine();
            log.info("使用命令 -> " + command);
            switch (command) {
                case "onlines":
                    //onlines(stateListener);
                    break;
                case "clear":
                    clear();
                    break;
                case "restart":
                case "stop":
                    log.info("正在关闭..");
                    scanner.close();
                    log.info("已关闭服务端");
                    if (command.equals("restart"))
                        //restart();
                    System.exit(0);
                    break;
                case "?":
                case "help":
                    //allCommand();
                    break;
                default:
                    log.info("未知命令 -> " + command);
                    log.info("输入 ? 或 help 查看所有命令");
                    break;
            }
        }
    }

    // 读取配置文件
    private static Config getConfig(String path) {
        Wini ini = null;
        try {
            ini = new Wini(new File(path));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        String root = ini.get("dev","root");
        String photo = ini.get("dev","photo");
        String navList = ini.get("dev","navList");
        String publicFile = ini.get("dev","publicFile");
        Boolean compressImg = Boolean.valueOf(ini.get("dev","compressImg"));
        Integer portPublic = Integer.valueOf(ini.get("dev","portPublic"));
        Integer portState = Integer.valueOf(ini.get("dev","portState"));
        Integer portUpload = Integer.valueOf(ini.get("dev","portUpload"));
        Integer portDownload = Integer.valueOf(ini.get("dev","portDownload"));
        Config config = new Config(root, photo, navList, publicFile, compressImg, portPublic, portState, portUpload, portDownload);

        return config;
    }

    // 清空屏幕
    private static void clear() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }



}
