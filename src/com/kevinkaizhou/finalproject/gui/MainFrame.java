package com.kevinkaizhou.finalproject.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;

import com.kevinkaizhou.finalproject.CheckerController;

public class MainFrame extends JFrame implements MessageShower,ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton iodButton;
	private JTextField iodTextField;
	private JButton petriButton;
	private JTextField petriTextField;
	private JTextArea consoleArea;
	private JTextArea outputArea;
	private JButton checkButton;
	
	private JFileChooser chooser;
	private File iodFile;
	private File petriFile;
	
	private CheckerController controller;
	
	public MainFrame() {
		super("consistency checker");
		init();
		controller = new CheckerController();
		this.setSize(809,500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	@Override
	public void consoleMessage(String message) {
		if (consoleArea!=null) {
			consoleArea.append(">>> "+message+"\r\n");
			consoleArea.setCaretPosition(consoleArea.getText().length()); 
		}
	}

	@Override
	public void outputMessage(String message) {
		if (outputArea!=null) {
			outputArea.append(message+"\r\n");
			outputArea.setCaretPosition(outputArea.getText().length()); 
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(iodButton) || e.getSource().equals(petriButton)) {
			if (chooser == null) {
				chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Only XML file is approved!", "xml");
				chooser.setFileFilter(filter);
			}
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (e.getSource().equals(iodButton)) {
					iodFile = chooser.getSelectedFile();
					iodTextField.setText(iodFile.getAbsolutePath());
					consoleMessage("choose IOD file: "+iodFile.getName());
				}else {
					petriFile = chooser.getSelectedFile();
					petriTextField.setText(petriFile.getAbsolutePath());
					consoleMessage("choose petri file: "+petriFile.getName());
				}
			}
		}else if (e.getSource().equals(checkButton)) {
			if (iodFile == null) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(this, "No IOD file choose!", "ERROR",JOptionPane.ERROR_MESSAGE);
			}else if(petriFile == null) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(this, "No Petri net file choose!", "ERROR",JOptionPane.ERROR_MESSAGE);
			}else {
				controller.check(iodFile, petriFile, this);
			}
		}
	}
	
	private void init() {
		JPanel containerPanel = new JPanel();
		containerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		containerPanel.setLayout(gridBagLayout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		//组件之间间距
		c.ipadx = 20;
		c.ipady = 20;
		//“添加IOD文件”label
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		JLabel iodLabel = new JLabel("IOD xml file:");
		containerPanel.add(iodLabel);
		gridBagLayout.setConstraints(iodLabel, c);
		//“选择文件”button
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		iodButton = new JButton("...");
		iodButton.addActionListener(this);
		containerPanel.add(iodButton);
		gridBagLayout.setConstraints(iodButton,c);
		//显示iod文件地址
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weightx = 0.5;
		c.weighty = 0;
		iodTextField = new JTextField();
//iodTextField.setText("/Users/zhoukai/Documents/final project/iod.xml");
		iodTextField.setEditable(false);
		containerPanel.add(iodTextField);
		gridBagLayout.setConstraints(iodTextField,c);
		//“添加Petri net xml文件”label
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		JLabel petriLabel = new JLabel("Petri net xml file:");
		containerPanel.add(petriLabel);
		gridBagLayout.setConstraints(petriLabel, c);
		//“选择文件”button
		c.gridx = 3;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		petriButton = new JButton("...");
		petriButton.addActionListener(this);
		containerPanel.add(petriButton);
		gridBagLayout.setConstraints(petriButton,c);
		//显示petri文件地址
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weightx = 0.5;
		c.weighty = 0;
		petriTextField = new JTextField();
//petriTextField.setText("/Users/zhoukai/Documents/final project/project.xml");
		petriTextField.setEditable(false);
		containerPanel.add(petriTextField);
		gridBagLayout.setConstraints(petriTextField,c);
		
		//console panel
		c.gridx = 4;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 4;
		c.weightx = 0.5;
		c.weighty = 0;
		consoleArea = new JTextArea(">>> console...\r\n");
		consoleArea.setLineWrap(true);
		consoleArea.setWrapStyleWord(true);
		consoleArea.setEditable(false);
		consoleArea.setCaretPosition( consoleArea.getDocument().getLength());
		JScrollPane jScrollPane = new JScrollPane(consoleArea);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		containerPanel.add(jScrollPane);
		gridBagLayout.setConstraints(jScrollPane, c);

		//check button
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		checkButton = new JButton("check!");
		checkButton.addActionListener(this);
		containerPanel.add(checkButton);
		gridBagLayout.setConstraints(checkButton,c);
		
		//output label
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 7;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 0;
		JLabel outputLabel = new JLabel("output:");
		containerPanel.add(outputLabel);
		gridBagLayout.setConstraints(outputLabel, c);
		
		//output panel
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 7;
		c.gridheight = 3;
		c.weightx = 1;
		c.weighty = 1;
		outputArea = new JTextArea();
		outputArea.setLineWrap(true);
		outputArea.setWrapStyleWord(true);
		outputArea.setEditable(false);
		JScrollPane jScrollPane2 = new JScrollPane(outputArea);
		jScrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		containerPanel.add(jScrollPane2);
		gridBagLayout.setConstraints(jScrollPane2, c);
		
		this.add(containerPanel);
	}

}
