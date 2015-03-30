package testGUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Test extends JFrame{
	
	
	public Test() {
		this.setSize(new Dimension(100, 100));
//		this.setLayout(new GridLayout());
		this.getContentPane().add(new JFileChooser());
		this.setLocation(0, 0);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Test();
	}
}
