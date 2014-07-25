package com.zephyrr.gaspread.test;

import java.io.IOException;
import java.net.Socket;

import com.zephyrr.gaspread.ai.ClientSideGA;
import com.zephyrr.gaspread.net.Connection;

public class ClientMain {
	public static void main(String[] args) {
		Socket sock = null;
		try {
			sock = new Socket("localhost", 9001);
		} catch(IOException e) {
			e.printStackTrace();
		}
		Connection c = new Connection(sock);
		ClientSideGA gsga = new OnesClient(c);
		gsga.run();
	}
}
