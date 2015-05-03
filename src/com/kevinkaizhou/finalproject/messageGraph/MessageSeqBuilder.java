package com.kevinkaizhou.finalproject.messageGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import com.kevinkaizhou.finalproject.messageGraph.MessageGraph.Edge;

/**
 * 
 * @author zhoukai
 * 利用拓扑图获得所有可能的排列
 */
public class MessageSeqBuilder {
	Vector<String> messages;
	Stack<String> tempSeq;
	MessageGraph messageGraph;
	
	Vector<List<String>> messageSeqs;
	
	public MessageSeqBuilder(Vector<String> messages) {
		this.messages=messages;
		tempSeq = new Stack<>();
		messageGraph = new MessageGraph();
		messageSeqs = new Vector<>();
		for (String message : messages) {
			messageGraph.addNode(message);
		}
	}
	
	public void addConstraint(String prev,String next) {
		messageGraph.addEdge(prev, next);
	}
	
	public Vector<List<String>> getSeqs() {
		dfs(messageGraph);
		return messageSeqs;
	}
	
	private void dfs(MessageGraph G) {
		if (G.numberOfNodes() > 0 && G.nodesWithoutIn().size() == 0) {
			System.err.println("拓扑图存在环，限制条件自相矛盾");
			return;
		}
		if (G.numberOfNodes() == 0) {
			recordStack();
		}
		for (String node : G.nodesWithoutIn()) {
			Vector<Edge> vector = G.deleteNode(node);
			tempSeq.push(node);
			dfs(G);
			tempSeq.pop();
			G.addNode(node);
			for (Edge edge : vector) {
				G.addEdge(edge);
			}
		}
		
	}
	
	private void recordStack() {
		List<String> tempList = new ArrayList<>(tempSeq);
		messageSeqs.add(tempList);
	}
	
	public static void main(String[] args) {
		Vector<String> vector = new Vector<>();
		vector.add("not_approved!");vector.add("not_approved?");vector.add("not_possible!");vector.add("not_possible?");vector.add("option!");vector.add("option?");
		MessageSeqBuilder builder = new MessageSeqBuilder(vector);
		builder.addConstraint("not_approved!", "not_approved?");
		builder.addConstraint("not_possible!", "not_possible?");
		builder.addConstraint("option!", "option?");
		builder.addConstraint("not_approved?", "not_possible!");
		builder.addConstraint("not_approved?", "option!");
		builder.addConstraint("not_possible!", "option!");
		builder.addConstraint("not_possible?", "option?");
		System.out.println(builder.getSeqs());
	}
}
