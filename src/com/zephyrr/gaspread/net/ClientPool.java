package com.zephyrr.gaspread.net;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ClientPool {
	private Queue<Task> todo;
	private Queue<Connection> available;
	private Map<Integer, Connection> working;
	public ClientPool() {
		todo = new LinkedList<Task>();
		available = new LinkedList<Connection>();
		working = new HashMap<Integer, Connection>();
	}
	public void enqueueTask(Task t) {
		todo.add(t);
	}
	public void registerConnection(Connection c) {
		available.add(c);
	}
	public Map<Integer, String> doTasks() {
		Map<Integer, String> results = new HashMap<Integer, String>();
		Set<Integer> completed = new HashSet<Integer>();
		while(!todo.isEmpty() || !working.isEmpty()) {
			for(int i : working.keySet()) {
				Connection c = working.get(i);
				String ret = c.retrieve();
				if(ret != null) {
					completed.add(i);
					results.put(i, ret);
				}
			}
			for(int i : completed) {
				available.add(working.get(i));
				working.remove(i);
			}
			completed.clear();
			while(!available.isEmpty() && !todo.isEmpty()) {
				Connection worker = available.remove();
				Task t = todo.remove();
				working.put(t.getID(), worker);
				worker.sendMessageSig(t.getTaskString());
			}
		}
		return results;
	}
}
