package com.ezasm;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

class TabbedPaneTest {

	private static final JFrame f = new JFrame();
	
	static {
		final var p1 = new JPanel();
		final var ta = new JTextArea(200,200);
		ta.setEditable(true);
		p1.add(ta);
		final var b = new JButton("test");
		p1.add(b);

		final var p2 = new JPanel();

		final var p3 = new JPanel();

		final var tp = new JTabbedPane();
		tp.setBounds(50, 50, 200, 200);
		tp.add("main",p1);
		tp.add("visit",p2);
		tp.add("help",p3);

		f.add(tp);
		f.setSize(400, 400);
		f.setLayout(null);
		f.setVisible(true);
	}

	public static void main(String[] args) {}

}
