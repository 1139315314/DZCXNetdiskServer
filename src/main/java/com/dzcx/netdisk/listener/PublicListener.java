package com.dzcx.netdisk.listener;

import com.dzcx.netdisk.socket.PublicSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class PublicListener extends Thread {
	
	private int port = -1;
	
	public PublicListener(int port) {
		this.port = port;
	}
	
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				Socket socket = serverSocket.accept();
				PublicSocket requestSocket = new PublicSocket(socket);
				requestSocket.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}