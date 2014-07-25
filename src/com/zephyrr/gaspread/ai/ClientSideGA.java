package com.zephyrr.gaspread.ai;

import java.util.ArrayList;
import java.util.List;

import com.zephyrr.gaspread.net.Connection;

public abstract class ClientSideGA {
	private Connection c;
	public ClientSideGA(Connection c) {
		if(!c.isAlive())
			c.start();
		this.c = c;
	}
	public void run() {
		while(true) {
			String in = c.retrieve();
			if(in == null)
				continue;
			List<Boolean> member = new ArrayList<Boolean>();
			String[] bools = in.split(" ");
			for(String s : bools) {
				member.add(Boolean.parseBoolean(s));
			}
			int fitness = getFitness(member);
			c.sendMessageSig("" + fitness);
		}
	}
	public abstract int getFitness(List<Boolean> member);
}
