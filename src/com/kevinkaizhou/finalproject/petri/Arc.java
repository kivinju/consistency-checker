package com.kevinkaizhou.finalproject.petri;

public class Arc {
	private String sourceid;
	private String targetid;
	
	public Arc(String sourceid, String targetid) {
		super();
		this.sourceid = sourceid;
		this.targetid = targetid;
	}
	
	public String getSourceid() {
		return sourceid;
	}
	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}
	public String getTargetid() {
		return targetid;
	}
	public void setTargetid(String targetid) {
		this.targetid = targetid;
	}
	
	@Override
	public String toString() {
		return sourceid+"-->"+targetid;
	}
}
