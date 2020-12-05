package com.dzcx.netdisk.listener;

import com.dzcx.netdisk.socket.DownloadSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DownloadListener extends Thread {
	
	private int port = -1;
	
	public DownloadListener(int port) {
		this.port = port;
	}
	
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				Socket socket = serverSocket.accept();
				DownloadSocket requestSocket = new DownloadSocket(socket);
				requestSocket.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}