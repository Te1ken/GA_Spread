package com.zephyrr.gaspread.test;

import java.util.List;

import com.zephyrr.gaspread.ai.ClientSideGA;
import com.zephyrr.gaspread.net.Connection;

public class OnesClient extends ClientSideGA {
	public OnesClient(Connection c) {
		super(c);
	}

	@Override
	public int getFitness(List<Boolean> member) {
		int count = 0;
		for(Boolean b : member) {
			if(b) {
				count++;
			}
		}
		return count;
	}
}
