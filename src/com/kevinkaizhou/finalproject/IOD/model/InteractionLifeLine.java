package com.kevinkaizhou.finalproject.IOD.model;

import java.util.*;

public class InteractionLifeLine {
	private String id;
	private String name;
	
	//activation��Ϊ�������ϵ��¼���
	private List<String> activations;
	
	public InteractionLifeLine(String id, String name, List<String> activations) {
		this.id = id;
		this.name = name;
		this.activations = activations;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<String> getActivations() {
		return activations;
	}
	
	
}
