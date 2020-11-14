package com.dzcx.netdisk.entity;

public class Config {

    private String root;

    private String photo;

    private String navList;

    private String publicFile;

    private boolean compressImg;

    private int portPublic;

    private int portState;

    private int portUpload;

    private int portDownload;

    public Config() {
    }

    public Config(String root, String photo, String navList, String publicFile, boolean compressImg, int portPublic, int portState, int portUpload, int portDownload) {
        this.root = root;
        this.photo = photo;
        this.navList = navList;
        this.publicFile = publicFile;
        this.compressImg = compressImg;
        this.portPublic = portPublic;
        this.portState = portState;
        this.portUpload = portUpload;
        this.portDownload = portDownload;
    }

    @Override
    public String toString() {
        return "Config{" +
                "root='" + root + '\'' +
                ", photo='" + photo + '\'' +
                ", navList='" + navList + '\'' +
                ", publicFile='" + publicFile + '\'' +
                ", compressImg=" + compressImg +
                ", portPublic=" + portPublic +
                ", portState=" + portState +
                ", portUpload=" + portUpload +
                ", portDownload=" + portDownload +
                '}';
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNavList() {
        return navList;
    }

    public void setNavList(String navList) {
        this.navList = navList;
    }

    public String getPublicFile() {
        return publicFile;
    }

    public void setPublicFile(String publicFile) {
        this.publicFile = publicFile;
    }

    public boolean isCompressImg() {
        return compressImg;
    }

    public void setCompressImg(boolean compressImg) {
        this.compressImg = compressImg;
    }

    public int getPortPublic() {
        return portPublic;
    }

    public void setPortPublic(int portPublic) {
        this.portPublic = portPublic;
    }

    public int getPortState() {
        return portState;
    }

    public void setPortState(int portState) {
        this.portState = portState;
    }

    public int getPortUpload() {
        return portUpload;
    }

    public void setPortUpload(int portUpload) {
        this.portUpload = portUpload;
    }

    public int getPortDownload() {
        return portDownload;
    }

    public void setPortDownload(int portDownload) {
        this.portDownload = portDownload;
    }
}
