package com.zephyrr.gaspread.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zephyrr.gaspread.ai.ServerSideGA;
import com.zephyrr.gaspread.net.GAServer;

public class OnesServer extends ServerSideGA<String> {

	public OnesServer(GAServer server) {
		super(2, 1, 20, 300, server);
	}

	@Override
	public List<List<Boolean>> cross(List<List<Boolean>> parents, int childCount) {
		List<List<Boolean>> children = new ArrayList<List<Boolean>>();
		for(int i = 0; i < childCount; i++) {
			List<Integer> crossPoints = new ArrayList<Integer>();
			for(int pt = 0; pt < parents.size() - 1; pt++) {
				crossPoints.add((int)(Math.random() * parents.get(0).size()));
			}
			Collections.sort(crossPoints);
			Collections.shuffle(parents);
			List<Boolean> child = new ArrayList<Boolean>();
			int prev = 0;
			int count = 0;
			for(int breakpt : crossPoints) {
				child.addAll(parents.get(count).subList(prev, breakpt));
				prev = breakpt;
				count++;
			}
			child.addAll(parents.get(count).subList(prev, parents.get(count).size()));
			children.add(child);
		}
		return children;
	}

	@Override
	public List<Boolean> toBinary(String in) {
		List<Boolean> member = new ArrayList<Boolean>();
		String[] data = in.split(" ");
		for(String s : data) {
			member.add(Boolean.parseBoolean(s));
		}
		return member;
	}

	@Override
	public String toMember(List<Boolean> bin) {
		String member = "";
		for(Boolean b : bin) {
			member += b + " ";
		}
		member = member.trim();
		return member;
	}
}
