package com.ezasm;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

final class NewSettingsMenu extends JFrame {
	private static final Insets INSETS = new Insets(5, 5, 5, 5);
	private static final Border BORDER = BorderFactory.createEmptyBorder(5, 5, 5, 5);

	static final JButton saveButton = new JButton("Save Changes");
	static final JButton resetToDefaultsButton = new JButton("Reset to Defaults");

	private static GridBagConstraints quickConstraints(int gridx, int gridy) {
		final var c = new GridBagConstraints();
		c.insets = INSETS;
		c.gridx = gridx;
		c.gridy = gridy;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		return c;
	}

	private static void fillEmptyVerticalSpace(JPanel panel) {
		final var c = new GridBagConstraints();
		c.gridy = panel.getComponentCount();
		c.gridwidth = 2;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		panel.add(new JPanel(), c);
	}

	private static JPanel createAppearanceTab() {
		final var fontSizeLabel = new JLabel("Font Size");
		final var fontSizeTextField = new JTextField("fontsize");
		final var themeLabel = new JLabel("Theme");
		final var themeDropdown = new JComboBox<>(new String[] { "theme 1", "theme 2" });

		final var appearanceTab = new JPanel(new GridBagLayout());
		appearanceTab.setBorder(BORDER);

		appearanceTab.add(fontSizeLabel, quickConstraints(0, 0));
		appearanceTab.add(fontSizeTextField, quickConstraints(1, 0));
		appearanceTab.add(themeLabel, quickConstraints(0, 1));
		appearanceTab.add(themeDropdown, quickConstraints(1, 1));

		fillEmptyVerticalSpace(appearanceTab);

		return appearanceTab;
	}

	static JPanel createExecutionTab() {
		final var speedLabel = new JLabel("Instruction Delay");
		final var speedSlider = new JSlider(1, 100, 69);
		
		final var executionTab = new JPanel(new GridBagLayout());
		executionTab.setBorder(BORDER);

		executionTab.add(speedLabel, quickConstraints(0, 0));
		executionTab.add(speedSlider, quickConstraints(1, 0));

		fillEmptyVerticalSpace(executionTab);

		return executionTab;
	}

	static JTabbedPane createdTabbedPane() {
		final var tp = new JTabbedPane(JTabbedPane.LEFT);
		tp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		//tp.add("General", generalTab);
		tp.add("Appearance", createAppearanceTab());
		tp.add("Execution", createExecutionTab());
		return tp;
	}

	static JPanel createMainPanel() {
		final var mainPanel = new JPanel(new GridBagLayout());

		var c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 1;
		mainPanel.add(createdTabbedPane(), c);

		c = new GridBagConstraints();
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		mainPanel.add(buttonsRow(saveButton, resetToDefaultsButton), c);

		return mainPanel;
	}

	private static JPanel buttonsRow(JButton... buttons) {
		final var row = new RightFlowPanel();
		for (final var button : buttons)
			row.add(button);
		return row;
	}

	NewSettingsMenu() {
		super("EzASM Settings");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(createMainPanel());
		validate();
		pack();
	}
}
