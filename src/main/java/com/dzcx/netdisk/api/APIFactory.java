package com.dzcx.netdisk.api;

public class APIFactory {

    public static API getApi() {
        return new Proxy();
    }

}
