package com.zephyrr.gaspread.net;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Connection extends Thread {
	private Socket sock;
	private BufferedReader reader;
	private BufferedOutputStream writer;
	private volatile Queue<String> toSend;
	private volatile Queue<String> result;
	public Connection(Socket sock) {
		this.sock = sock;
		toSend = new LinkedList<String>();
		result = new LinkedList<String>();
		try {
			sock.setKeepAlive(true);
			sock.setSoTimeout(10);
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			writer = new BufferedOutputStream(sock.getOutputStream());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		while(sock.isConnected()) {
			String incoming = getMessage();
			if(incoming != null) {
				result.add(incoming);
			}
			while(!toSend.isEmpty()) {
				sendMessage(toSend.remove());
			}
		}
	}
	public String retrieve() {
		if(result.isEmpty()) {
			return null;
		}
		return result.remove();
	}
	public void sendMessageSig(String taskString) {
		toSend.add(taskString);
	}
	private void sendMessage(String str) {
		try {
			byte[] bytes = (str + "\r\n").getBytes();
			writer.write(bytes);
			writer.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	private String getMessage() {
		try {
			return reader.readLine();
		} catch (IOException e) {}
		return null;
	}
}
