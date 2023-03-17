package com.ezasm;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

class TabbedPaneTest {

	static {
		
		//buttonActionListener = new ButtonActionListener();
		final var popup = new JFrame("EzASM Configurator");
		popup.setMinimumSize(new Dimension(500, 300));
		popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		final var themeLabel = new JLabel("Theme");
		final var themeInput = new JComboBox<>(new String[] { "test1", "test2" });
		themeInput.setSelectedItem("test1");

		final var fontSizeLabel = new JLabel("Font Size");
		final var speedLabel = new JLabel("Instruction Delay");

		final var fontInput = new JTextField("font input");
		final var speedSlider = new JSlider(10, 1000, 69);

		final var generalPanel = new JPanel(new BoxLayout(null, BoxLayout.Y_AXIS));

		final var fontSizePanel = new JPanel(new FlowLayout());
		fontSizePanel.add(fontSizeLabel);
		fontSizePanel.add(fontInput);
		generalPanel.add(fontSizePanel);

		final var speedPanel = new JPanel(new FlowLayout());
		speedPanel.add(speedLabel);
		speedPanel.add(speedSlider);
		generalPanel.add(speedPanel);

		final var themePanel = new JPanel(new FlowLayout());
		themePanel.add(themeLabel);
		themePanel.add(themeInput);
		generalPanel.add(themePanel);

		final var save = new JButton("Save Changes");
		final var resetDefaults = new JButton("Reset to Defaults");

		final var btnLayout = new FlowLayout(FlowLayout.RIGHT, 5, 5);
		final var buttonsRow = new JPanel(btnLayout);
		generalPanel.add(buttonsRow);
		buttonsRow.add(save);
		buttonsRow.add(resetDefaults);

		final var tp = new JTabbedPane(JTabbedPane.LEFT);
		tp.setBounds(0, 0, popup.getWidth(), popup.getHeight());

		tp.add("General", generalPanel);

		//resetDefaults.addActionListener(buttonActionListener);
		//save.addActionListener(buttonActionListener);

		popup.add(tp);

		popup.validate();
		popup.pack();
		popup.setVisible(true);
	}

	public static void main(String[] args) {}

}
