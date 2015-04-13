package com.kevinkaizhou.finalproject.graph;

import java.util.*;

public class PathRecorder {
	private Map<String, Boolean> marked;
	private Vector<List<String>> paths;
	private Vector<List<String>> loops;
	
	private Graph graph;
	private List<String> tempPath;
	
	public PathRecorder(Graph graph) {
		this.marked = new HashMap<String, Boolean>();
		paths = new Vector<>();
		loops = new Vector<>();
		
		this.tempPath = new ArrayList<>();
		this.graph=graph;
		for (String start : graph.getStartVertices()) {
			dfs(start);
		}
	}
	
	private void dfs(String v) {
		marked.put(v, true);
		tempPath.add(v);
		for (String w : graph.adj(v)) {
			//到了尾节点
			if(graph.getEndVertices().contains(w)){
				List<String> path = new ArrayList<>(tempPath);
				path.add(w);
				paths.add(path);
			}
			//遇到循环
			else if (marked.containsKey(w) && marked.get(w)) {
				List<String> loop = new ArrayList<>();
				boolean tempRecord=false;
				for (String string : tempPath) {
					if (tempRecord) {
						loop.add(string);
						continue;
					}
					if (string.equals(w)) {
						tempRecord=true;
						loop.add(string);
					}
				}
				loop.add(w);
				loops.add(loop);
			}
			//没走到过，走下去
			else{
				dfs(w);
			}
		}
		tempPath.remove(tempPath.size()-1);
		marked.put(v, false);
	}
	
	
	
	public Vector<List<String>> getPaths() {
		return paths;
	}

	public Vector<List<String>> getLoops() {
		return loops;
	}

	public static void main(String[] args) {
		Vector<String> vertices = new Vector<>();
		vertices.add("1");
		vertices.add("2");
		vertices.add("3");
		vertices.add("4");
		vertices.add("5");
		
		Vector<String> startVertices = new Vector<>();
		startVertices.add("1");
		Vector<String> endVertices = new Vector<>();
		endVertices.add("5");
		
		Graph graph = new Graph(vertices, startVertices, endVertices);
		graph.addEdge("1", "2");
		graph.addEdge("1", "3");
		graph.addEdge("2", "4");
		graph.addEdge("3", "5");
		graph.addEdge("4", "1");
		graph.addEdge("4", "3");
//		graph.addEdge("5", "4");
		
		PathRecorder pathRecorder = new PathRecorder(graph);
		System.out.println(pathRecorder.getPaths());
		System.out.println(pathRecorder.getLoops());
	}
}
