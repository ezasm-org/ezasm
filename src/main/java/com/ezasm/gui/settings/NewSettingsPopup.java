package com.ezasm.gui.settings;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

public final class NewSettingsPopup extends JFrame {
	private static final Insets INSETS = new Insets(5, 5, 5, 5);
	private static final Border BORDER = BorderFactory.createEmptyBorder(5, 5, 5, 5);

	private static GridBagConstraints quickConstraints(int gridx, int gridy) {
		final var c = new GridBagConstraints();
		c.insets = INSETS;
		c.gridx = gridx;
		c.gridy = gridy;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		return c;
	}

	private static void fillWithEmptyVerticalSpace(JPanel panel) {
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

		fillWithEmptyVerticalSpace(appearanceTab);

		return appearanceTab;
	}

	private static JPanel createExecutionTab() {
		final var speedLabel = new JLabel("Instruction Delay");
		final var speedSlider = new JSlider(1, 100, 69);

		final var executionTab = new JPanel(new GridBagLayout());
		executionTab.setBorder(BORDER);

		executionTab.add(speedLabel, quickConstraints(0, 0));
		executionTab.add(speedSlider, quickConstraints(1, 0));

		fillWithEmptyVerticalSpace(executionTab);

		return executionTab;
	}

	private static JTabbedPane createdTabbedPane() {
		final var tp = new JTabbedPane(JTabbedPane.LEFT);
		tp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		// tp.add("General", generalTab);
		tp.add("Appearance", createAppearanceTab());
		tp.add("Execution", createExecutionTab());
		return tp;
	}

	private JPanel createButtonsPanel() {
		final var saveButton = new JButton("Save Changes");
		saveButton.addActionListener((final var e) -> {
			try {
				config.setFontSize(Integer.parseInt(fontSizeInput.getText()));
			} catch (NumberFormatException er) {
				JOptionPane.showMessageDialog(new JFrame(), "Bad format for font size, please input a number");
				return;
			}
			if (autoSaveButton.getSliderValue() == 0) {
				config.setAutoSaveInterval(1);
				instance.autoSaveButton.setToggleButtonStatus(false);
			}
			instance.config.setSimSpeed(instance.speedSlider.getValue());
			instance.config.setTabSize(instance.tabSizeSlider.getValue());
			instance.config.setTheme(instance.themeInput.getSelectedItem().toString());
			instance.config.setAutoSaveInterval(instance.autoSaveButton.getSliderValue());
			instance.config.setAutoSaveSelected(instance.autoSaveButton.getToggleButtonStatus());
			instance.config.saveChanges();
			instance.applyTheme(new Font(Config.DEFAULT_FONT, Font.PLAIN, instance.config.getFontSize()),
					EditorTheme.getTheme(instance.config.getTheme()));
			Window.getInstance().applyConfiguration(instance.config);
		});

		final var resetToDefaultsButton = new JButton("Reset to Defaults");
		resetToDefaultsButton.addActionListener((final var e) -> {

		});

		final var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		panel.add(saveButton);
		panel.add(resetToDefaultsButton);

		return panel;
	}

	private static JPanel createMainPanel() {
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
		mainPanel.add(createButtonsPanel(), c);

		mainPanel.setBackground(null);

		return mainPanel;
	}

	private final Config config;
	private final JTextField fontSizeInput = new JTextField("fontsize");
	private final JComboBox<String> themeDropdown = new JComboBox<>(new String[] { "theme 1", "theme 2" });
	private final JSlider speedSlider = new JSlider(1, 100, 69);
	private final AutoSaveSliderToggleButton autoSaveInput = new AutoSaveSliderToggleButton(
			config.getAutoSaveSelected(), config.getAutoSaveInterval());

	public NewSettingsPopup(final Config config) {
		super("EzASM Settings");
		this.config = config;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(createMainPanel());
		validate();
		pack();
	}
}
