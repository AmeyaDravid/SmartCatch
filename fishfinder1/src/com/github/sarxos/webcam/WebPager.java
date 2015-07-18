package com.github.sarxos.webcam;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class WebPager extends JFrame {
	
	public WebPager () {
		JEditorPane jep = new JEditorPane();
		jep.setEditable(false);   
	//look out for the ip address
	//make a button for ip address changer in console
		try {
		  jep.setPage("http://10.0.0.250");
		}catch (IOException e) {
		  jep.setContentType("text/html");
		  jep.setText("<html>Could not load</html>");
		} 
	
		JScrollPane scrollPane = new JScrollPane(jep);     
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(scrollPane);
		setPreferredSize(new Dimension(800,600));
		setVisible(true);
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

			WebPager wp = new WebPager();
			wp.setVisible(true);
	}

}
