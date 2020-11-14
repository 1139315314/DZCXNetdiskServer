package com.dzcx.netdisk.socket;

import com.dzcx.netdisk.Main;
import com.dzcx.netdisk.entity.ServerStatus;
import com.dzcx.netdisk.listener.StateListener;
import com.google.gson.Gson;
import org.hyperic.sigar.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class StateSocket extends Thread {

    private Logger log;
    private Socket socket;
    private Sigar sigar = new Sigar();
    private StateListener listener;

    public StateSocket(StateListener listener, Socket socket) {
        this.listener = listener;
        this.socket = socket;
        this.log = Main.log;
    }

    @Override
    public void run() {
        OutputStream os = null;
        try {
            os = socket.getOutputStream();
            while (true) {
                os.write((new Gson().toJson(getServerStatus()) + "\r\n").getBytes("UTF-8"));
                sleep(1000);
            }
        } catch (Exception e) {
            log.info("State socket 断开连接");
        } finally {
            listener.remove(socket);
            try {
                os.close();
            } catch (IOException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return 获取服务器的一些状态信息
     */
    private ServerStatus getServerStatus() {
        ServerStatus status = new ServerStatus();
        try {
            Mem mem = sigar.getMem();
            CpuPerc cp = sigar.getCpuPerc();
            FileSystem fs = sigar.getFileSystemList()[1];
            FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());

            status.setCpuUse(cp.getCombined());
            status.setMemUse(mem.getUsed());
            status.setMemMax(mem.getTotal());
            status.setDiskUse(usage.getUsed());
            status.setDiskMax(usage.getTotal());
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return status;
    }


}
