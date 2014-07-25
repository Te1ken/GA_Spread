package com.zephyrr.gaspread.test;

import java.util.ArrayList;
import java.util.List;

import com.zephyrr.gaspread.ai.ServerSideGA;
import com.zephyrr.gaspread.net.GAServer;

public class ServerMain {
	public static void main(String[] args) {
		GAServer gas = new GAServer(9001);
		gas.start();
		ServerSideGA<String> ssga = new OnesServer(gas);
		List<String> pop = new ArrayList<String>();
		for(int i = 0; i < 20; i++) {
			String member = "";
			for(int g = 0; g < 32; g++) {
				member += (Math.random() < .5) + " ";
			}
			pop.add(member);
		}
		System.out.println(ssga.evolve(pop, 1500));
	}
}
