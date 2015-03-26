package com.kevinkaizhou.finalproject.petri;

import java.io.*;
import java.util.*;

import org.jdom2.input.*;
import org.jdom2.*;

public class Petri {
	private Map<String, String> places;
	private Map<String, String> transitions;
	private List<Arc> arcs;
	private List<String> initialPlaces;
	
	public Petri(){
		places=new HashMap<String, String>();
		transitions=new HashMap<String, String>();
		arcs=new ArrayList<Arc>();
		initialPlaces=new ArrayList<String>();
	}
	
	public void parse(String path) {
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(new File(path));
			Element rootEl = doc.getRootElement();
			rootEl = rootEl.getChild("net");
			List<Element> placelist = rootEl.getChildren("place");
			for (Element element : placelist) {
				String id=element.getAttributeValue("id");
				String value=element.getChild("name").getChildText("value");
				if (element.getChild("initialMarking").getChildText("value").equals("1")) {
					initialPlaces.add(id);
				}
				places.put(id, value);
			}
			List<Element> transitionlist = rootEl.getChildren("transition");
			for (Element element : transitionlist) {
				String id=element.getAttributeValue("id");
				String value=element.getChild("name").getChildText("value");
				transitions.put(id, value);
			}
			List<Element> arclist = rootEl.getChildren("arc");
			for (Element element : arclist) {
				String source=element.getAttributeValue("source");
				String target=element.getAttributeValue("target");
				arcs.add(new Arc(source, target));
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Vector<String> toTransitions(String fromTransitionId) {
		Vector<String> result = new Vector<>();
		Vector<String> places = new Vector<>();
		for (Arc arc : arcs) {
			if (arc.getSourceid().equals(fromTransitionId)) {
				places.add(arc.getTargetid());
			}
		}
		for (Arc arc : arcs) {
			if (places.contains(arc.getSourceid())) {
				result.add(arc.getTargetid());
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		return places+"\n\n"+transitions+"\n\n"+arcs+"\n\n"+"initial:"+initialPlaces;
	}
	
	//test
	public static void main(String[] args) {
		Petri p=new Petri();
		p.parse("Petri net 1.xml");
		System.out.println(p);
		System.out.println(p.toTransitions("card!"));
	}

	public boolean check(List<String> seq, String testpointer) {
		if (!testpointer.equals("")) {
			if(!toTransitions(testpointer).contains(seq.get(0))){
				return false;
			}
		}
		for (int i = 0; i < seq.size()-1; i++) {
			if (!toTransitions(seq.get(i)).contains(seq.get(i+1))) {
				return false;
			}
		}
		return true;
	}
}
