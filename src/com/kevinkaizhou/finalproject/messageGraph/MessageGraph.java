package com.kevinkaizhou.finalproject.messageGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MessageGraph {

	private Map<String, Node> nodes;
	private Vector<Edge> edges;
	
	public MessageGraph() {
		nodes = new HashMap<String, MessageGraph.Node>();
		edges = new Vector<>();
		
	}
	
	public void addNode(String name) {
		Node node = new Node(name);
		nodes.put(name, node);
	}
	public void addEdge(String from, String to) {
		Edge edge = new Edge(from, to);
		nodes.get(from).outPlus();
		nodes.get(to).inPlus();
		edges.add(edge);
	}
	public void addEdge(Edge edge) {
		nodes.get(edge.getFrom()).outPlus();
		nodes.get(edge.getTo()).inPlus();
		edges.add(edge);
	}
	public Vector<Edge> deleteNode(String name) {
		Vector<Edge> deletedEdges = new Vector<>();
		for (Edge edge : edges) {
			if (edge.getFrom().equals(name)) {
				nodes.get(edge.getTo()).inMinus();
			}else if (edge.getTo().equals(name)) {
				nodes.get(edge.getFrom()).outMinus();
			}else {
				continue;
			}
			deletedEdges.add(edge);
		}
		edges.removeAll(deletedEdges);
		nodes.remove(name);
		return deletedEdges;
	}
	public int numberOfNodes() {
		return nodes.size();
	}
	public Vector<String> nodesWithoutIn() {
		Vector<String> result = new Vector<>();
		for (Map.Entry<String, Node> entry : nodes.entrySet()) {
			if (entry.getValue().getIn() == 0) {
				result.add(entry.getValue().getName());
			}
		}
		return result;
	}
	
	class Node {
		private String name;
		private int in;
		private int out;
		
		public Node(String name) {
			this.name = name;
			this.in = 0;
			this.out = 0;
		}
		
		public void inPlus() {
			++in;
		}
		
		public void inMinus() {
			if (in>0) {
				--in;
			}
		}
		
		public void outPlus() {
			++out;
		}
		
		public void outMinus() {
			if (out>0) {
				--out;
			}
		}

		public String getName() {
			return name;
		}

		public int getIn() {
			return in;
		}

		public int getOut() {
			return out;
		}
		
	}
	
	class Edge {
		private String from;
		private String to;
		
		public Edge(String from, String to) {
			this.from = from;
			this.to = to;
		}

		public String getFrom() {
			return from;
		}

		public String getTo() {
			return to;
		}
		
	}
}
