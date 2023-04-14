package com.ezasm;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

class VBoxPanel extends JPanel {
	public VBoxPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
}

class RightFlowPanel extends JPanel {
	public RightFlowPanel() {
		super(new FlowLayout(FlowLayout.RIGHT));
	}

	public RightFlowPanel(int hgap, int vgap) {
		super(new FlowLayout(FlowLayout.RIGHT, hgap, vgap));
	}
}
