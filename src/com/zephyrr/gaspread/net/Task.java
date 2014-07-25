package com.zephyrr.gaspread.net;

public class Task {
	private int tid;
	private String taskString;
	public Task(int id, String str) {
		tid = id;
		taskString = str;
	}
	public Integer getID() {
		return tid;
	}
	public String getTaskString() {
		return taskString;
	}
}
