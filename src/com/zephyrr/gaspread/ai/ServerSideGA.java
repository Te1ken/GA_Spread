package com.zephyrr.gaspread.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zephyrr.gaspread.net.GAServer;
import com.zephyrr.gaspread.net.Task;

public abstract class ServerSideGA<T> {
	private int parentCount, childCount, breeds, mutateChance;
	private GAServer server;
	public ServerSideGA(int parents, int children, int breeds, int mutateChance, GAServer server) {
		this.parentCount = parents;
		this.childCount = children;
		this.breeds = breeds;
		this.mutateChance = mutateChance;
		this.server = server;
	}
	
	private List<List<Boolean>> selectParents(  int parentCount, 
												List<List<Boolean>> population,
												List<Integer> thresholds) {
		List<List<Boolean>> ret = new ArrayList<List<Boolean>>();
		for(int i = 0; i < parentCount; i++) {
			List<Boolean> parent;
			do {
				parent = null;
				int activation = (int)(Math.random() * thresholds.get(thresholds.size() - 1));
				for(int index = 0; index < thresholds.size() && parent == null; index++) {
					if(activation < thresholds.get(index)) {
						parent = population.get(index);
					}
				}
			} while(ret.contains(parent));
			ret.add(parent);
		}
		return ret;
	}
	
	private List<Integer> getFitnesses(List<List<Boolean>> population) {
		List<Integer> fitnesses = new ArrayList<Integer>();
		List<Task> tasks = new ArrayList<Task>();
		for(int i = 0; i < population.size(); i++) {
			String out = "";
			for(Boolean b : population.get(i)) {
				out += b + " ";
			}
			Task t = new Task(i, out);
			tasks.add(t);
		}
		Map<Integer, String> results = server.evaluate(tasks);
		for(int i = 0; i < population.size(); i++) {
			fitnesses.add(Integer.parseInt(results.get(i)));
		}
		System.out.println("Fitnesses: " + fitnesses);
		return fitnesses;
	}
	
	private List<Integer> getThresholds(List<List<Boolean>> population) {
		List<Integer> fitnesses = getFitnesses(population);
		int total = 0;
		List<Integer> thresholds = new ArrayList<Integer>();
		for(Integer fitness : fitnesses) {
			total += fitness;
			thresholds.add(total);
		}
		return thresholds;
	}
	
	private List<Boolean> mutate(List<Boolean> child) {
		for(int i = 0; i < child.size(); i++) {
			if((int)(Math.random() * mutateChance) == 1) {
				child.set(i, !child.get(i));
			}
		}
		return child;
	}
	
	private List<List<Boolean>> generation(List<List<Boolean>> parents) {
		List<List<Boolean>> children = new ArrayList<List<Boolean>>();
		List<Integer> thresholds = getThresholds(parents);
		for(int b = 0; b < breeds; b++) {
			List<List<Boolean>> procreators = selectParents(parentCount, parents, thresholds);
			List<List<Boolean>> crossed = cross(procreators, childCount);
			for(List<Boolean> pre : crossed) {
				children.add(mutate(pre));
			}
		}
		return children;
	}
	
	public List<T> evolve(List<T> population, int generations) {
		List<List<Boolean>> binPop = new ArrayList<List<Boolean>>();
		for(T member : population) {
			binPop.add(toBinary(member));
		}
		
		for(int gen = 0; gen < generations; gen++) {
			System.out.println("Generation " + gen);
			binPop = generation(binPop);
		}
		
		List<T> finalPop = new ArrayList<T>();
		for(List<Boolean> bin : binPop) {
			finalPop.add(toMember(bin));
		}
		return finalPop;
	}
	
	public abstract List<List<Boolean>> cross(List<List<Boolean>> parents, int childCount);
	public abstract List<Boolean> toBinary(T in);
	public abstract T toMember(List<Boolean> bin);
}
