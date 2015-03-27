package com.kevinkaizhou.finalproject;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import com.kevinkaizhou.finalproject.IOD.model.ControlFlow;
import com.kevinkaizhou.finalproject.IOD.model.Frame;
import com.kevinkaizhou.finalproject.IOD.model.IOD;
import com.kevinkaizhou.finalproject.IOD.model.Interaction;
import com.kevinkaizhou.finalproject.graph.Graph;
import com.kevinkaizhou.finalproject.graph.PathRecorder;
import com.kevinkaizhou.finalproject.petri.Petri;

public class Main {
	public static IOD iod;
	public static Petri petri;
	public static PathRecorder pathRecorder;
	
	public static void main(String[] args) {
		iod = new IOD("project.xml");
		Vector<String> vertices = new Vector<>(iod.getInteractionMap().keySet());
		vertices.addAll(iod.getInitialNodeMap().keySet());
		vertices.addAll(iod.getFinalNodeMap().keySet());
		
		Graph iodGraph = new Graph(vertices, new Vector<>(iod.getInitialNodeMap().keySet()), new Vector<>(iod.getFinalNodeMap().keySet()));
		for (Map.Entry<String, ControlFlow> controlFlow : iod.getControlFlowMap().entrySet()) {
			iodGraph.addEdge(controlFlow.getValue().getFrom(), controlFlow.getValue().getTo());
		}
		
		pathRecorder = new PathRecorder(iodGraph);
		
		petri=new Petri();
		petri.parse("Petri net 1.xml");
		
//		System.out.println("Paths:");
//		for (List<String> path : pathRecorder.getPaths()) {
//			printPath(path);
//		}
//		System.out.println("Loops:");
//		for (List<String> loop : pathRecorder.getLoops()) {
//			printPath(loop);
//		}
		System.out.println(check());
	}
	
	
	private static Stack<String> stack = new Stack<>();
	
	private static boolean check() {
		String iodpointer;
		
		for (List<String> path : pathRecorder.getPaths()) {
			String pipepointer = "";
			stack.clear();
			if (dfs(path, 0, pipepointer)) {
				System.out.println("find seq:"+stack);
				return true;
			}
		}
		return false;
	}
	
	private static boolean dfs(List<String> path, int pointer , String pipepointer) {
		String interactionName = getVName(path.get(pointer));
		if (interactionName.equals("initial")) {
			stack.push("initial");
			if (dfs(path, pointer+1, pipepointer)){
				return true;
			}
		}
		if (interactionName.equals("final")) {
			stack.push("final");
			return true;
		}
		Frame frame = iod.getFrameMap().get(interactionName);
		String testpointer = pipepointer;
		for (List<String> seq : frame.getSeqs()) {
			stack.addAll(seq);
			//在pipe上可不可以走
			if(!petri.check(seq,testpointer)){
				continue;
			}
			testpointer = seq.get(seq.size()-1);
			if (dfs(path, pointer+1, testpointer)){
				return true;
			}
			stack.removeAll(seq);
			testpointer = pipepointer;
		}
		return false;
	}
	
	private static void printPath(List<String> path) {
		for (String string : path) {
			System.out.print( getVName(string)+ " -> ");
		}
		System.out.println("");
	}
	private static String getVName(String id) {
		if (iod.getInitialNodeMap().containsKey(id)) {
			return "initial";
		}else if(iod.getFinalNodeMap().containsKey(id)){
			return "final";
		}else if(iod.getInteractionMap().containsKey(id)){
			return iod.getInteractionMap().get(id).getName();
		}
		return "";
	}
}
