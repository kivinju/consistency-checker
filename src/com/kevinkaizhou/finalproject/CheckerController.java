package com.kevinkaizhou.finalproject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.kevinkaizhou.finalproject.IOD.ControlFlow;
import com.kevinkaizhou.finalproject.IOD.Frame;
import com.kevinkaizhou.finalproject.IOD.IOD;
import com.kevinkaizhou.finalproject.graph.Graph;
import com.kevinkaizhou.finalproject.petri.Petri;

public class CheckerController {

	// Model
	public IOD iod;
	public Petri petri;
	public Map<String, String> marked;
	public Graph graph;
	
	// View
	MessageShower shower;
	
	public void check(File iodFile, File petriFile, MessageShower shower) {
		this.shower = shower;
		shower.consoleMessage("load IOD file...");
		iod = new IOD(iodFile.getAbsolutePath());
		Vector<String> vertices = new Vector<>(iod.getInteractionMap().keySet());
		vertices.addAll(iod.getInitialNodeMap().keySet());
		vertices.addAll(iod.getFinalNodeMap().keySet());

		marked = new HashMap<String, String>();

		graph = new Graph(vertices, new Vector<>(iod.getInitialNodeMap()
				.keySet()), new Vector<>(iod.getFinalNodeMap().keySet()));
		for (Map.Entry<String, ControlFlow> controlFlow : iod
				.getControlFlowMap().entrySet()) {
			graph.addEdge(controlFlow.getValue().getFrom(), controlFlow
					.getValue().getTo());
		}
		shower.consoleMessage("load completed");
		shower.consoleMessage("load Petri net file...");
		petri = new Petri();
		petri.parse(petriFile.getAbsolutePath());
		shower.consoleMessage("load completed");

		shower.consoleMessage("check consistency...");
		boolean b = check();
		shower.consoleMessage("check completed");
		if (!b) {
			shower.outputMessage("not consistent");
		}
	}
	
	private boolean check() {
		for (String start : iod.getInitialNodeMap().keySet()) {
			if(dfs(start,"")){
				System.out.println(tempPath);
				shower.outputMessage(tempPath.toString());
				return true;
			}
		}
		return false;
	}
	
	public static List<String> tempPath = new ArrayList<>();
	
	// 主要函数：深度优先遍历
	// v-interaction id
	private boolean dfs(String v,String pipePointer) {
		String interactionName = getVName(v);
		if (interactionName.equals("initial")) {
			tempPath.add("initial");
			for (String w : graph.adj(v)) {
				if (dfs(w, pipePointer)) {
					return true;
				}
			}
			return false;
		}
		if (interactionName.equals("final")) {
			tempPath.add("final");
			return true;
		}
		Frame frame = iod.getFrameMap().get(interactionName);
		for (List<String> seq : frame.getSeqs()) {
			String tid = petri.getTransitionId(seq.get(0));
			//petri图中已走过，不会重新走
			if (marked.containsKey(v) && marked.get(v).equals(tid)) {
				continue;
			}
			marked.put(v, tid);
			tempPath.addAll(seq);
			// check这个方法用来验证这个sequence在petri网中能否走通
			if (petri.check(seq, pipePointer)) {
				pipePointer = seq.get(seq.size()-1);
				for (String w : graph.adj(v)) {
					if (dfs(w, pipePointer)) {
						return true;
					}
				}
			}
			tempPath.removeAll(seq);
			marked.remove(v);
		}
		return false;
	}

	private String getVName(String id) {
		if (iod.getInitialNodeMap().containsKey(id)) {
			return "initial";
		}else if(iod.getFinalNodeMap().containsKey(id)){
			return "final";
		}else if(iod.getInteractionMap().containsKey(id)){
			return iod.getInteractionMap().get(id).getName();
		}
		return "";
	}
	
	public static void main(String[] args) {
		new MainFrame();
	}
}
