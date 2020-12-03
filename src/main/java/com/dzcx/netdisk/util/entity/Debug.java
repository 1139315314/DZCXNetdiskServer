package com.dzcx.netdisk.util.entity;

import javax.swing.*;
import java.awt.*;

public class Debug extends JDialog {

	private static final long serialVersionUID = 1L;
	private TextArea textArea;

	public Debug() {
		setTitle("IUtils Debug");
		setSize(450, 300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		
		textArea = new TextArea();
		textArea.setFont(new Font("Simsun", Font.PLAIN, 14));
		getContentPane().add(textArea, BorderLayout.CENTER);
	}
	
	public void setDebug(Object content) {
		textArea.setText(content.toString());
	}
}
