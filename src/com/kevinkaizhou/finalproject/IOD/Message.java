package com.kevinkaizhou.finalproject.IOD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message implements Comparable<Message>{
	private String id;
	private String name;
	
	//出发&接收的lifeline
	private String fromEnd;
	private String toEnd;
	
	//出发&接收的activation
	private String fromActivation;
	private String toActivation;
	
	private String SequenceNumber;

	public Message(String Id) {
		this.id = Id;
		this.SequenceNumber = Id;
	}
	
	public Message(String Id, String name, String fromEnd, String toEnd, String fromActivation, String toActivation, String sequence) {
		this.id = Id;
		this.name = name;
		this.fromEnd = fromEnd;
		this.toEnd = toEnd;
		this.fromActivation = fromActivation;
		this.toActivation = toActivation;
		this.SequenceNumber = sequence;
	}
	
	//根据SequenceNumber比较两个message大小
	@Override
	public int compareTo(Message o) {
		String[] s1 = SequenceNumber.split("\\.");
		String[] s2 = o.getSequenceNumber().split("\\.");
		for (int i = 0; i < Math.min(s1.length, s2.length); i++) {
			int i1 = Integer.parseInt(s1[i]);
			int i2 = Integer.parseInt(s2[i]);
			if (i1 == i2) {
				continue;
			}
			return i1 - i2;
		}
		return s1.length-s2.length;
	}

	public String getId() {
		return id;
	}

	public String getFromEnd() {
		return fromEnd;
	}

	public String getToEnd() {
		return toEnd;
	}

	public String getFromActivation() {
		return fromActivation;
	}

	public String getToActivation() {
		return toActivation;
	}

	public String getSequenceNumber() {
		return SequenceNumber;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return SequenceNumber;
	}
	
	public String getName() {
		return name;
	}

//	public static void main(String[] args) {
////		String[] temp = "1.2.3.4".split("\\.");
////		System.out.println(Arrays.toString(temp));
//		Message m1 = new Message("1.2");
//		Message m2 = new Message("1.1.1");
//		List<Message> list = new ArrayList<>();
//		list.add(m1);list.add(m2);
//		System.out.println(list);
//		Collections.sort(list);
//		System.out.println(list);
//	}
}
