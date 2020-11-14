package com.dzcx.netdisk.test;

import com.dzcx.netdisk.entity.Config;
import org.ini4j.Wini;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class TestSomethings {

    private static final Logger logger = LoggerFactory.getLogger(TestSomethings.class);

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        Wini ini = null;
        try {
            ini = new Wini(new File("src/main/resources/iNetdiskServer.ini"));
        } catch (IOException e) {
            logger.error(e.getMessage());
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

        System.out.println(config);
    }


    public static void testlog() {
        logger.debug("debug");
        logger.warn("warm");
        logger.error("error");
    }

}
