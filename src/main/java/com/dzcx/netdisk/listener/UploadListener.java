package com.dzcx.netdisk.listener;

import com.dzcx.netdisk.socket.UploadSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class UploadListener extends Thread {
	
	private int port = -1;
	
	public UploadListener(int port) {
		this.port = port;
	}
	
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				Socket socket = serverSocket.accept();
				UploadSocket requestSocket = new UploadSocket(socket);
				requestSocket.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}