package com.ezasm;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.ezasm.ConvenienceClasses.RightFlowPanel;
import com.ezasm.ConvenienceClasses.VBoxPanel;

// https://www.youtube.com/watch?v=g2vDARb7gx8
final class NewSettingsMenu {
	
	static final JLabel themeLabel = new JLabel("Theme");
	static final JComboBox<String> themeDropdown = new JComboBox<>(new String[] { "theme 1", "theme 2" });

	static final JLabel fontSizeLabel = new JLabel("Font Size");
	static final JTextField fontSizeTextField = new JTextField("fontsize");

	static final JLabel speedLabel = new JLabel("Instruction Delay");
	static final JSlider speedSlider = new JSlider(1, 100, 69);

	static final JButton saveButton = new JButton("Save Changes");
	static final JButton resetToDefaultsButton = new JButton("Reset to Defaults");

	static final JPanel generalTab = new JPanel();
	static final JPanel appearanceTab = new JPanel();
	static final JPanel executionTab = new JPanel();

	static final JTabbedPane tp = new JTabbedPane(JTabbedPane.LEFT);

	static final JFrame window = new JFrame("Preferences");

	static GridBagConstraints constraints(int gridx, int gridy) {
		final var c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = gridx;
		c.gridy = gridy;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		return c;
	}

	static void setupAppearanceTab() {
		appearanceTab.setLayout(new GridBagLayout());

		appearanceTab.add(fontSizeLabel, constraints(0, 0));
		appearanceTab.add(fontSizeTextField, constraints(1, 0));
		appearanceTab.add(themeLabel, constraints(0, 1));
		appearanceTab.add(themeDropdown, constraints(1, 1));
	}

	static void setupExecutionTab() {
		executionTab.setLayout(new GridBagLayout());

		executionTab.add(speedLabel, constraints(0, 0));
		executionTab.add(speedSlider, constraints(1, 0));
	}

	static {
		window.setPreferredSize(new Dimension(500, 500));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setupAppearanceTab();
		setupExecutionTab();

		tp.add("General", generalTab);
		tp.add("Appearance", appearanceTab);
		tp.add("Execution", executionTab);
		tp.setBounds(0, 0, window.getWidth(), window.getHeight());

		tp.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				window.pack();
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
			
		});

		final var windowLayout = new VBoxPanel();
		windowLayout.add(tp);
		windowLayout.add(buttonsRow(saveButton, resetToDefaultsButton));

		window.add(windowLayout);
		window.validate();
		window.pack();
		window.setVisible(true);
	}

	private static JPanel buttonsRow(JButton... buttons) {
		final var row = new RightFlowPanel();
		for (final var button : buttons)
			row.add(button);
		return row;
	}

	public static void main(String[] __) {}

}
