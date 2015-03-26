package com.kevinkaizhou.finalproject.IOD.model;

import java.util.ArrayList;
import java.util.List;

public class Interaction {
	private String id;
	private String name;
	
	private List<String> FromSimpleRelationships;
	private List<String> ToSimpleRelationships;
	
	public Interaction(String Id, String name) {
		this.id = Id;
		this.name = name;
		
		FromSimpleRelationships = new ArrayList<>();
		ToSimpleRelationships = new ArrayList<>();
	}
	
	public void addFromRelationship(String fromRelationship) {
		FromSimpleRelationships.add(fromRelationship);
	}
	public void addToRelationship(String toRelationship) {
		ToSimpleRelationships.add(toRelationship);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<String> getFromSimpleRelationships() {
		return FromSimpleRelationships;
	}

	public List<String> getToSimpleRelationships() {
		return ToSimpleRelationships;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Id:"+id+" name:"+name;
	}
}
