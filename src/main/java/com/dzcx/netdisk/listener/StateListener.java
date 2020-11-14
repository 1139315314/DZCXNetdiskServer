package com.dzcx.netdisk.listener;

import com.dzcx.netdisk.Main;
import com.dzcx.netdisk.socket.StateSocket;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class StateListener extends Thread {

    private int port = -1;
    private List<Socket> onlines;
    private Logger log;

    public StateListener(int port) {
        this.port = port;
        this.onlines = new ArrayList<>();
        this.log = Main.log;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                // 移除掉之前已经断开连接的
                for (int i = 0; i < onlines.size(); i++) {
                    if (onlines.get(i).isClosed()) onlines.remove(i);
                }
                Socket socket = serverSocket.accept();
                onlines.add(socket);
                log.info("新的链接建立，IP：" + socket.getInetAddress().getHostAddress());

                StateSocket stateSocket = new StateSocket(this, socket);
                stateSocket.start();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

    }

    public List<Socket> getOnlines() {
        return onlines;
    }

    public void remove(Socket socket) {
        this.onlines.remove(socket);
    }
}
