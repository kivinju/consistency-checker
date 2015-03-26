package com.kevinkaizhou.finalproject.IOD.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Frame {
	private String id;
	private String name;
	
	private List<InteractionLifeLine> interactionLifeLines;
	private Vector<List<String>> seqs;
	
	public Frame(String id, String name) {
		this.id = id;
		this.name = name;
		
		interactionLifeLines = new ArrayList<>();
	}
	
	public void addInteractionLifeLine(InteractionLifeLine lifeLine) {
		interactionLifeLines.add(lifeLine);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<InteractionLifeLine> getInteractionLifeLines() {
		return interactionLifeLines;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\n Id:"+id+" name:"+name + " seqs: "+seqs;
	}
	
	public Vector<List<String>> getSeqs() {
		return seqs;
	}

	public void setSeqs(Vector<List<String>> seqs) {
		this.seqs = seqs;
	}

	public boolean containLifeline(String lifelineId) {
		for (InteractionLifeLine lifeLine : interactionLifeLines) {
			if (lifeLine.getId().equals(lifelineId)) {
				return true;
			}
		}
		return false;
	}
}
