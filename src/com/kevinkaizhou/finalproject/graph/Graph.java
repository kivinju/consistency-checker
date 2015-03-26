package com.kevinkaizhou.finalproject.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Graph {
	private int V;
	private int E;
	
	private Vector<String> vertices;
	private Vector<String> startVertices;
	private Vector<String> endVertices;
	private Map<String, Vector<String>> adj;


	public Graph(Vector<String> vertices,Vector<String> startVertices,Vector<String> endVertices) {
		V=vertices.size();
		E=0;
		this.vertices=vertices;
		this.startVertices=startVertices;
		this.endVertices=endVertices;
		
		this.adj=new HashMap<>();
		for (String vertex : vertices) {
			adj.put(vertex, new Vector<String>());
		}
	}
	
	public void addEdge(String from, String to) {
		adj.get(from).add(to);
		++E;
	}
	
	public Iterable<String> adj(String vertex) {
		return adj.get(vertex);
	}

	public int getV() {
		return V;
	}

	public int getE() {
		return E;
	}

	public Vector<String> getVertices() {
		return vertices;
	}

	public Vector<String> getStartVertices() {
		return startVertices;
	}

	public Vector<String> getEndVertices() {
		return endVertices;
	}
	
}
