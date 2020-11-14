package com.dzcx.netdisk.api;

import com.dzcx.netdisk.Main;
import org.slf4j.Logger;

public class Proxy implements API {

    private Logger log;
    private APIImpl api;

    public Proxy() {
        this.log = Main.log;
        this.api = new APIImpl();
    }



}
