package com.zephyrr.gaspread.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class GAServer extends Thread {
	private ClientPool clients;
	private ServerSocket ss;
	public GAServer(int port) {
		clients = new ClientPool();
		try {
			ss = new ServerSocket(port);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(!ss.isClosed()) {
			Socket s = null;
			try {
				s = ss.accept();
			} catch(IOException e) {
				e.printStackTrace();
			}
			Connection c = new Connection(s);
			c.start();
			clients.registerConnection(c);
		}
	}
	
	public Map<Integer, String> evaluate(List<Task> tasks) {
		for(Task t : tasks) {
			clients.enqueueTask(t);
		}
		return clients.doTasks();
	}
}
