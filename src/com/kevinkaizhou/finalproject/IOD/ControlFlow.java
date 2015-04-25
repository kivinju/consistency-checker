package com.kevinkaizhou.finalproject.IOD;

public class ControlFlow {
	private String id;
	//来时的interaction
	private String from;
	//去时的interaction
	private String to;
	
	public ControlFlow(String id, String from, String to) {
		this.id = id;
		this.from = from;
		this.to = to;
	}

	public String getId() {
		return id;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "From:"+from+" to:"+to+" id:"+id;
	}
	
}
