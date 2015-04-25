package deprecate;

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

public class Main {
	public static IOD iod;
	public static Petri petri;
//	public static PathRecorder pathRecorder;
	
	//key-interactionId value-对应的Petri图中的tid
	public static Map<String, String> marked;
	public static Graph graph;
	
	
	public static void main(String[] args) {
		iod = new IOD("project.xml");
		Vector<String> vertices = new Vector<>(iod.getInteractionMap().keySet());
		vertices.addAll(iod.getInitialNodeMap().keySet());
		vertices.addAll(iod.getFinalNodeMap().keySet());
		
		marked = new HashMap<String, String>();
		
		graph = new Graph(vertices, new Vector<>(iod.getInitialNodeMap().keySet()), new Vector<>(iod.getFinalNodeMap().keySet()));
		for (Map.Entry<String, ControlFlow> controlFlow : iod.getControlFlowMap().entrySet()) {
			graph.addEdge(controlFlow.getValue().getFrom(), controlFlow.getValue().getTo());
		}
		
//		pathRecorder = new PathRecorder(iodGraph);
		
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
	
	
//	private static Stack<String> stack = new Stack<>();
	
	/**
	 * 
	 * @author zhoukai
	 * 
	 * 深度优先遍历，如果走过同一个地方，返回。
	 * 
	 */
	private static boolean check() {
		for (String start : iod.getInitialNodeMap().keySet()) {
			if(dfs(start,"")){
				System.out.println(tempPath);
				return true;
			}
		}
		return false;
	}
	
	public static List<String> tempPath = new ArrayList<>();
	
	// v-interaction id
	private static boolean dfs(String v,String pipePointer) {
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
			//到循环
			if (marked.containsKey(v) && marked.get(v).equals(tid)) {
				continue;
			}
			marked.put(v, tid);
			tempPath.addAll(seq);
			//验证此seq是否能走通
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
	
	/**
	 * @author zhoukai
	 * 
	 * 抛弃方法，原来是遍历每一条路径，再加上循环的路径。这样会导致搜索不全。
	 */
//	private static boolean check() {
//		String iodpointer;
//		
//		for (List<String> path : pathRecorder.getPaths()) {
//			String pipepointer = "";
//			stack.clear();
//			if (dfs(path, 0, pipepointer)) {
//				System.out.println("find seq:"+stack);
//				return true;
//			}
//		}
//		return false;
//	}
	
//	private static boolean dfs(List<String> path, int pointer , String pipepointer) {
//		String interactionName = getVName(path.get(pointer));
//		if (interactionName.equals("initial")) {
//			stack.push("initial");
//			if (dfs(path, pointer+1, pipepointer)){
//				return true;
//			}
//			return false;
//		}
//		if (interactionName.equals("final")) {
//			stack.push("final");
//			return true;
//		}
//		
//		String testpointer = pipepointer;
//		for (List<String> seq : frame.getSeqs()) {
//			stack.addAll(seq);
//			//在pipe上可不可以走
//			if(!petri.check(seq,testpointer)){
//				continue;
//			}
//			testpointer = seq.get(seq.size()-1);
//			if (dfs(path, pointer+1, testpointer)){
//				return true;
//			}
//			stack.removeAll(seq);
//			testpointer = pipepointer;
//		}
//		return false;
//	}
	
//	private static void printPath(List<String> path) {
//		for (String string : path) {
//			System.out.print( getVName(string)+ " -> ");
//		}
//		System.out.println("");
//	}
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
