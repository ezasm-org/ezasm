package com.ezasm.gui.settings;

import java.awt.FlowLayout;
import java.awt.Font;
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

import com.ezasm.gui.Window;
import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.EzSettingsTab;

// Entirely static class because it does not make sense to be able to have instances of a window
// So don't implement IThemeable, just call applyTheme on this class
public final class NewSettingsPopup {
	// Constants
	private static final Insets INSETS_5 = new Insets(5, 5, 5, 5);
	private static final Border BORDER_5 = BorderFactory.createEmptyBorder(5, 5, 5, 5);

	// Quick way to get GridBagConstraints for making forms with labels and components
	/**
	 * Quick way to get GridBagConstraints for making forms with labels and components
	 * 
	 * @param gridx 
	 * @param gridy
	 * @return
	 */
	private static GridBagConstraints quickConstraints(int gridx, int gridy) {
		final var c = new GridBagConstraints();
		c.insets = INSETS_5;
		c.gridx = gridx;
		c.gridy = gridy;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		return c;
	}

	/**
	 * Just an empty panel to fill the space below the main components in each of the tab panels
	 */
	private static final JPanel emptySpace = new JPanel();

	// Keeps the components above it at the top of the window, disregarding window resizing
	private static void fillWithEmptyVerticalSpace(JPanel panel) {
		final var c = new GridBagConstraints();
		c.gridy = panel.getComponentCount();
		c.gridwidth = 2;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		panel.add(emptySpace, c);
	}

	// The config object
	private static final Config config = new Config();

	// Here is everything that goes in their respective tabs:

	// Font size components
	private static final JLabel fontSizeLabel = new JLabel("Font Size");
	private static final JTextField fontSizeInput = new JTextField(String.valueOf(config.getFontSize()));;

	// Simulation speed / Instruction delay components
	private static final JLabel speedLabel = new JLabel("Instruction Delay");
	private static final JSlider speedSlider = new JSlider(10, 1000, config.getSimSpeed());

	// Tab size components
	private static final JLabel tabSizeLabel = new JLabel("Tab Size");
	private static final JSlider tabSizeSlider = new JSlider(1, 8, config.getTabSize());
	static {
		tabSizeSlider.setMajorTickSpacing(1);
		tabSizeSlider.setPaintTicks(true);
		tabSizeSlider.setPaintLabels(true);
	}

	// Auto save (enable/disable and interval) components
	private static final JLabel autoSaveLabel = new JLabel("Auto Save");
	private static final AutoSaveSliderToggleButton autoSaveButton
		= new AutoSaveSliderToggleButton(config.getAutoSaveSelected(), config.getAutoSaveInterval());

	// Theme components
	private static final JLabel themeLabel = new JLabel("Theme");
	private static final JComboBox<String> themeInput = new JComboBox<>(Config.THEMES);

	// Execution panel initialization and setup
	private static final EzSettingsTab executionTab = new EzSettingsTab();
	static {
		executionTab.setBorder(BORDER_5);
		executionTab.addMenuRow(speedLabel, speedSlider);
		executionTab.addVerticallyStretchedEmptyPanel();
	}

	// Appearance panel initialization and setup
	private static final JPanel appearanceTab = new JPanel(new GridBagLayout());
	static {
		appearanceTab.setBorder(BORDER_5);

		appearanceTab.add(fontSizeLabel, quickConstraints(0, 0));
		appearanceTab.add(fontSizeInput, quickConstraints(1, 0));
		appearanceTab.add(themeLabel, quickConstraints(0, 1));
		appearanceTab.add(themeInput, quickConstraints(1, 1));

		fillWithEmptyVerticalSpace(appearanceTab);
	}

	// Editor panel initialization and setup
	private static final JPanel editorTab = new JPanel(new GridBagLayout());
	static {
		editorTab.setBorder(BORDER_5);

		editorTab.add(tabSizeLabel, quickConstraints(0, 0));
		editorTab.add(tabSizeSlider, quickConstraints(1, 0));
		editorTab.add(autoSaveLabel, quickConstraints(0, 1));
		editorTab.add(autoSaveButton, quickConstraints(1, 1));

		fillWithEmptyVerticalSpace(editorTab);
	}

	// Create the JTabbedPane that holds all the tabs together
	private static JTabbedPane createTabbedPane() {
		final var tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tabbedPane.add("Appearance", appearanceTab);
		tabbedPane.add("Execution", executionTab);
		tabbedPane.add("Editor", editorTab);
		return tabbedPane;
	}

	// Now for the buttons below the tabbed pane:
	
	// The "Save Changes" button
	private static final JButton saveButton = new JButton("Save Changes");
	static {
		saveButton.addActionListener((final var e) -> {
			// check for non-integer input for font size
			try {
				config.setFontSize(Integer.parseInt(fontSizeInput.getText()));
			} catch (final NumberFormatException er) {
				JOptionPane.showMessageDialog(new JFrame(), "Bad format for font size, please input a number");
				return;
			}

			// if user slid to 0, disable auto save
			if (autoSaveButton.getSliderValue() == 0) {
				config.setAutoSaveInterval(1);
				autoSaveButton.setToggleButtonStatus(false);
			}

			// set the properties in the config
			config.setSimSpeed(speedSlider.getValue());
			config.setTabSize(tabSizeSlider.getValue());
			config.setTheme(themeInput.getSelectedItem().toString());
			config.setAutoSaveInterval(autoSaveButton.getSliderValue());
			config.setAutoSaveSelected(autoSaveButton.getToggleButtonStatus());

			// save to config file
			config.saveChanges();

			// apply new theming
			applyTheme(new Font(Config.DEFAULT_FONT, Font.PLAIN, config.getFontSize()),
			EditorTheme.getTheme(config.getTheme()));

			// apply config to main ezasm window
			Window.getInstance().applyConfiguration(config);
		});
	}

	// The "Reset to Defaults" button
	private static final JButton resetToDefaultsButton = new JButton("Reset to Defaults");
	static {
		resetToDefaultsButton.addActionListener((final var e) -> {
			config.resetDefaults();
			fontSizeInput.setText(Config.DEFAULT_FONT_SIZE);
			speedSlider.setValue(Integer.parseInt(Config.DEFAULT_SIMULATION_SPEED));
			tabSizeSlider.setValue(Integer.parseInt(Config.DEFAULT_TAB_SIZE));
			themeInput.setSelectedIndex(0);
			autoSaveButton.setToggleButtonStatus(Boolean.parseBoolean(Config.DEFAULT_AUTO_SAVE_SELECTED));
			autoSaveButton.setSliderValue(Integer.parseInt(Config.DEFAULT_AUTO_SAVE_INTERVAL));
		});
	}

	// Panel for the Save and Reset buttons
	private static JPanel createButtonsPanel() {
		final var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(saveButton);
		panel.add(resetToDefaultsButton);
		return panel;
	}

	// Main panel, holds everything together
	private static final JPanel mainPanel = new JPanel(new GridBagLayout());
	static {
		// Add the tabbed pane, filling all the vertical space
		var c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 1;
		mainPanel.add(createTabbedPane(), c);

		// The buttons and buttons panel are defined farther down
		// Let the panel have just the bottom of the window
		c = new GridBagConstraints();
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		mainPanel.add(createButtonsPanel(), c);
	}

	// Window
	private static final JFrame frame = new JFrame("EzASM Settings");
	static {
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(mainPanel);
		frame.validate();
		frame.pack();
	}

	public static void main(String[] args) {
		show();
	}

	// The only public methods:

	public static void show() { frame.setVisible(true); }
	public static void hide() { frame.setVisible(false); }

	public static void applyTheme(final Font font, final EditorTheme editorTheme) {
		//executionTab.applyTheme(font, editorTheme);
		editorTheme.applyTheme(executionTab);

		// get colors
		final var foreground = editorTheme.foreground();
		final var background = editorTheme.background();

		// create themed borders
        final var border = BorderFactory.createMatteBorder(1, 1, 1, 1, foreground);
        final var buttonBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, foreground);

		// apply background color to all panels
		editorTab.setBackground(background);
		appearanceTab.setBackground(background);
		executionTab.setBackground(background);
        mainPanel.setBackground(background);

		// apply foreground color to text fields
        fontSizeInput.setCaretColor(foreground);

		// make labels opaque?
        themeLabel.setOpaque(true);
        fontSizeLabel.setOpaque(true);
        speedLabel.setOpaque(true);
        tabSizeLabel.setOpaque(true);

		// more theming done elsewhere
        EditorTheme.applyFontThemeBorderless(speedSlider, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(themeInput, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(autoSaveButton, font, editorTheme);
        EditorTheme.applyFontThemeBorder(fontSizeInput, font, editorTheme, border);
        EditorTheme.applyFontThemeBorder(saveButton, font, editorTheme, buttonBorder);
        EditorTheme.applyFontThemeBorder(resetToDefaultsButton, font, editorTheme, buttonBorder);
        EditorTheme.applyFontThemeBorderless(speedLabel, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(fontSizeLabel, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(themeLabel, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(tabSizeLabel, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(autoSaveLabel, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(tabSizeSlider, font, editorTheme);
    }

	static {
		applyTheme(new Font(Config.DEFAULT_FONT, Font.PLAIN, config.getFontSize()), EditorTheme.getTheme(config.getTheme()));
	}
}
