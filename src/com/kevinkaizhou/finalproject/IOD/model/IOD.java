package com.kevinkaizhou.finalproject.IOD.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.kevinkaizhou.finalproject.messageGraph.MessageSeqBuilder;

public class IOD {
	//String是name, frame专注专注内部的lifeline等信息。interaction专注整张图的交互
	private Map<String, Frame> frameMap;
	
	//String均为id
	private Map<String, Interaction> interactionMap;
	private Map<String, Interaction> initialNodeMap;
	private Map<String, Interaction> finalNodeMap;
	private Map<String, ControlFlow> controlFlowMap;
	private Map<String, Message> messageMap;
	
	public IOD(String filepath) {
		frameMap = new HashMap<>();
		interactionMap = new HashMap<>();
		initialNodeMap = new HashMap<>();
		finalNodeMap = new HashMap<>();
		controlFlowMap = new HashMap<>();
		messageMap = new HashMap<>();
		
		parse(filepath);
	}
	
	private void parse(String filepath) {
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = builder.build(new File(filepath));
			Element root = document.getRootElement();
			Element models = root.getChild("Models");

			List<Element> frames = models.getChildren("Frame");
			List<Element> modelRelationshipContainers = models.getChild("ModelRelationshipContainer").getChild("ModelChildren").getChildren("ModelRelationshipContainer");
			List<Element> interactions = models.getChildren("Interaction");
			List<Element> initialNodes = models.getChildren("InitialNode");
			List<Element> finalNodes = models.getChildren("ActivityFinalNode");
			
//			System.out.println("frames:"+frames.size()+" interactions:"+interactions.size()+" initialNodes:"+initialNodes.size()+" finalNodes:"+finalNodes.size());
			
			parseFrames(frames);
			parseInteraction(interactions, initialNodes, finalNodes);
			
			List<Element> messageElements = null;
			List<Element> controlflowElements = null;
			List<Element> temp = modelRelationshipContainers.get(0).getChild("ModelChildren").getChildren();
			if (temp.get(0).getName().equals("Message")) {
				messageElements = temp;
			}else{
				controlflowElements = temp;
			}
			temp = modelRelationshipContainers.get(1).getChild("ModelChildren").getChildren();
			if (temp.get(0).getName().equals("Message")) {
				messageElements = temp;
			}else{
				controlflowElements = temp;
			}
			parseMessages(messageElements);
//			System.out.println("messages:"+messageElements.size());
			parseControlflow(controlflowElements);
//			System.out.println("controlflows:"+controlflowElements.size());
			
			//计算每个frame的seqs
			parseMessageSeqs();
			
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}
	
	//在每个frame中存入message的可能序列
	private void parseMessageSeqs() {
		for (Map.Entry<String, Frame> frameEntry : frameMap.entrySet()) {
			Frame frame = frameEntry.getValue();
			Vector<Message> messages = new Vector<>();
			for (Map.Entry<String, Message> messageEntry : messageMap.entrySet()) {
				Message message = messageEntry.getValue();
				if (frame.containLifeline(message.getFromEnd())) {
					messages.add(message);
				}
			}
			Vector<String> strings = new Vector<>(messages.size());
			for (Message message : messages) {
				strings.add(message.getName()+"!");
				strings.add(message.getName()+"?");
			}
			MessageSeqBuilder builder = new MessageSeqBuilder(strings);
			//限制 causality，每条message发在收之前
			for (Message message : messages) {
				builder.addConstraint(message.getName()+"!", message.getName()+"?");
			}
			//先将message按照sequence排序
			Collections.sort(messages);
			for (int i = 0; i < messages.size(); i++) {
				Message m1 = messages.get(i);
				for (int j = i+1; j < messages.size(); j++) {
					Message m2 = messages.get(j);
					//限制 controllability 同一条lifeline上收在发之前
					if (m1.getToEnd().equals(m2.getFromEnd())) {
						builder.addConstraint(m1.getName()+"?", m2.getName()+"!");
					}
					//限制 fifo order 
					if (m1.getFromEnd().equals(m2.getFromEnd()) && m1.getToEnd().equals(m2.getToEnd())) {
						builder.addConstraint(m1.getName()+"?", m2.getName()+"?");
					}
				}
			}
			frame.setSeqs(builder.getSeqs());
		}
	}
	
	private void parseMessages(List<Element> messageElements) {
		for (Element element : messageElements) {
			String id = element.getAttributeValue("Id");
			String name = element.getAttributeValue("Name");
			String fromEnd = element.getAttributeValue("EndRelationshipFromMetaModelElement");
			String toEnd = element.getAttributeValue("EndRelationshipToMetaModelElement");
			String fromActivation = element.getAttributeValue("FromActivation");
			String toActivation = element.getAttributeValue("ToActivation");
			String sequenceNumber = element.getAttributeValue("SequenceNumber");
			
			Message message = new Message(id, name, fromEnd, toEnd, fromActivation, toActivation, sequenceNumber);
			messageMap.put(id, message);
		}
	}
	
	private void parseControlflow(List<Element> controlflowElemets) {
		for (Element element : controlflowElemets) {
			String id = element.getAttributeValue("Id");
			String from = element.getAttributeValue("From");
			String to = element.getAttributeValue("To");
			
			ControlFlow controlFlow = new ControlFlow(id, from, to);
			controlFlowMap.put(id, controlFlow);
		}
	}
	
	public Map<String, Frame> getFrameMap() {
		return frameMap;
	}

	public Map<String, Interaction> getInteractionMap() {
		return interactionMap;
	}

	public Map<String, Interaction> getInitialNodeMap() {
		return initialNodeMap;
	}

	public Map<String, Interaction> getFinalNodeMap() {
		return finalNodeMap;
	}

	public Map<String, ControlFlow> getControlFlowMap() {
		return controlFlowMap;
	}

	public Map<String, Message> getMessageMap() {
		return messageMap;
	}

	private void parseFrames(List<Element> frames){
		for (Element element : frames) {
			String id = element.getAttributeValue("Id");
			String name = element.getAttributeValue("Name");
			Frame frame = new Frame(id, name);
			List<Element> lifelines = element.getChild("ModelChildren").getChildren("InteractionLifeLine");
			for (Element lifeline : lifelines) {
				String lifelineId = lifeline.getAttributeValue("Id");
				String lifelineName = lifeline.getAttributeValue("Name");
				List<String> activations = new ArrayList<>();
				if (lifeline.getChild("Activations")!=null) {
					List<Element> activationElements = lifeline.getChild("Activations").getChildren("Activation");
					for (Element activationElement : activationElements) {
						activations.add(activationElement.getAttributeValue("Id"));
					}
				}
				InteractionLifeLine interactionLifeLine = new InteractionLifeLine(lifelineId, lifelineName, activations);
				frame.addInteractionLifeLine(interactionLifeLine);
			}
			//frame用name做key！注意iod图中不允许出现重名的sd
			frameMap.put(name, frame);
		}
	}
	
	private void parseInteraction(List<Element> interactions, List<Element> initialNodes, List<Element> finalNodes) {
		for (Element interactionElement : interactions) {
			String interactionId = interactionElement.getAttributeValue("Id");
			String interactionName = interactionElement.getAttributeValue("Name");
			Interaction interaction = new Interaction(interactionId, interactionName);
			List<Element> fromSimpleRelationships = interactionElement.getChild("FromSimpleRelationships").getChildren("ControlFlow");
			for (Element element : fromSimpleRelationships) {
				interaction.addFromRelationship(element.getAttributeValue("Idref"));
			}
			List<Element> toSimpleRelationships = interactionElement.getChild("ToSimpleRelationships").getChildren("ControlFlow");
			for (Element element : toSimpleRelationships) {
				interaction.addToRelationship(element.getAttributeValue("Idref"));
			}
			interactionMap.put(interactionId, interaction);
		}
		for (Element interactionElement : initialNodes) {
			String interactionId = interactionElement.getAttributeValue("Id");
			String interactionName = interactionElement.getAttributeValue("Name");
			Interaction interaction = new Interaction(interactionId, interactionName);
			List<Element> fromSimpleRelationships = interactionElement.getChild("FromSimpleRelationships").getChildren("ControlFlow");
			for (Element element : fromSimpleRelationships) {
				interaction.addFromRelationship(element.getAttributeValue("Idref"));
			}
			initialNodeMap.put(interactionId, interaction);
		}
		for (Element interactionElement : finalNodes) {
			String interactionId = interactionElement.getAttributeValue("Id");
			String interactionName = interactionElement.getAttributeValue("Name");
			Interaction interaction = new Interaction(interactionId, interactionName);
			List<Element> toSimpleRelationships = interactionElement.getChild("ToSimpleRelationships").getChildren("ControlFlow");
			for (Element element : toSimpleRelationships) {
				interaction.addToRelationship(element.getAttributeValue("Idref"));
			}
			finalNodeMap.put(interactionId, interaction);
		}
	}
	
	public Interaction getInteraction(String id){
		if (initialNodeMap.containsKey(id)) {
			return initialNodeMap.get(id);
		}
		if (finalNodeMap.containsKey(id)) {
			return finalNodeMap.get(id);
		}
		if (interactionMap.containsKey(id)) {
			return interactionMap.get(id);
		}
		System.err.println("没找到interaction");
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("frameMap:"+frameMap+"\n");
		sb.append("interactionMap:"+interactionMap+"\n");
		sb.append("initialNodeMap:"+initialNodeMap+"\n");
		sb.append("finalNodeMap:"+finalNodeMap+"\n");
		sb.append("controlFlowMap:"+controlFlowMap+"\n");
		sb.append("messageMap:"+messageMap+"\n");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		IOD iod = new IOD("project.xml");
		System.out.println(iod);
	}
}
