package com.ezasm;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class ConvenienceClasses {
	
	public static class VBoxPanel extends JPanel {
		public VBoxPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		}
	}

	public static class HBoxPanel extends JPanel {
		public HBoxPanel() {
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		}
	}

	public static class LeftFlowPanel extends JPanel {
		public LeftFlowPanel() {
			super(new FlowLayout(FlowLayout.LEFT));
		}

		public LeftFlowPanel(int hgap, int vgap) {
			super(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
		}
	}

	public static class RightFlowPanel extends JPanel {
		public RightFlowPanel() {
			super(new FlowLayout(FlowLayout.RIGHT));
		}

		public RightFlowPanel(int hgap, int vgap) {
			super(new FlowLayout(FlowLayout.RIGHT, hgap, vgap));
		}
	}

}
