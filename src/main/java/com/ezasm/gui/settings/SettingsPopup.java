package com.ezasm.gui.settings;

import com.ezasm.gui.Window;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.EditorTheme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * The GUI settings popup. There can only be one of these instantiated at a time to avoid confusion. Allows the user to
 * configure and save their preferences about program operations.
 */
public class SettingsPopup implements IThemeable {

    private static SettingsPopup instance;

    private static final String FONTSIZE = "Font Size";
    private static final String SIMULATION_SPEED = "Instruction Delay";
    private static final String THEME = "Theme";
    private static final String TABSIZE = "Tab Size";
    private static final String AUTOSAVE = "Auto Save";
    private static final String LINEHIGHLIGHT = "Line Highlight";
    public static final String SAVE = "Save Changes";
    public static final String RESET = "Reset to Defaults";

    private JFrame popup;
    private JSlider speedSlider;
    private JSlider tabSizeSlider;
    private AutoSaveSliderToggleButton autoSaveButton;
    private JTextField fontInput;
    private JComboBox<String> themeInput;
    private JPanel grid;
    private JButton resetDefaults;
    private JButton save;
    private JButton lineHighlight;
    private JLabel speedLabel, fontSizeLabel, themeLabel, tabSizeLabel, autoSaveLabel, lineHighlightLabel;

    private ArrayList<String> LineHighlightOption = new ArrayList<String>(
            Arrays.asList("OFF", "Line Executed", "Line to Execute"));

    public final Config config;

    /**
     * Constructs a new singleton instance of the settings popup.
     */
    private SettingsPopup() {
        instance = this;
        config = new Config();
        initialize();
    }

    /**
     * Gets the running instance of the settings popup object.
     *
     * @return the running instance of the settings popup object.
     */
    public static SettingsPopup getInstance() {
        return instance;
    }

    /**
     * Instantiates this singleton if it does not already exist.
     */
    public static void instantiate() {
        if (instance == null || !instance.popup.isVisible())
            new SettingsPopup();
    }

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so.
     *
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     */
    public void applyTheme(Font font, EditorTheme editorTheme) {
        Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, editorTheme.foreground());
        Border buttonBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, editorTheme.foreground());
        grid.setBackground(editorTheme.background());
        fontInput.setCaretColor(editorTheme.foreground());
        themeLabel.setOpaque(true);
        fontSizeLabel.setOpaque(true);
        speedLabel.setOpaque(true);
        tabSizeLabel.setOpaque(true);
        EditorTheme.applyFontThemeBorderless(speedSlider, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(themeInput, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(autoSaveButton, font, editorTheme);
        EditorTheme.applyFontThemeBorder(fontInput, font, editorTheme, border);
        EditorTheme.applyFontThemeBorder(save, font, editorTheme, buttonBorder);
        EditorTheme.applyFontThemeBorder(lineHighlight, font, editorTheme, buttonBorder);
        EditorTheme.applyFontThemeBorder(resetDefaults, font, editorTheme, buttonBorder);
        EditorTheme.applyFontThemeBorderless(speedLabel, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(fontSizeLabel, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(themeLabel, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(tabSizeLabel, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(autoSaveLabel, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(tabSizeSlider, font, editorTheme);
        EditorTheme.applyFontThemeBorderless(lineHighlightLabel, font, editorTheme);
    }

    /**
     * Constructs the GUI elements of the settings popup.
     */
    private void initialize() {
        ButtonActionListener buttonActionListener = new ButtonActionListener();
        popup = new JFrame("EzASM Settings");
        popup.setLayout(new BorderLayout());
        popup.setMinimumSize(new Dimension(500, 300));
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        themeLabel = new JLabel(THEME);
        themeInput = new JComboBox<>(Config.THEMES);
        themeInput.setSelectedItem(config.getTheme());

        fontSizeLabel = new JLabel(FONTSIZE);
        speedLabel = new JLabel(SIMULATION_SPEED);

        fontInput = new JTextField(String.valueOf(config.getFontSize()));
        speedSlider = new JSlider(10, 1000, config.getSimSpeed());

        tabSizeLabel = new JLabel(TABSIZE);
        tabSizeSlider = new JSlider(1, 8, config.getTabSize());
        tabSizeSlider.setMajorTickSpacing(1);
        tabSizeSlider.setPaintTicks(true);
        tabSizeSlider.setPaintLabels(true);

        autoSaveLabel = new JLabel(AUTOSAVE);
        autoSaveButton = new AutoSaveSliderToggleButton(config.getAutoSaveSelected(), config.getAutoSaveInterval());

        lineHighlightLabel = new JLabel(LINEHIGHLIGHT);
        lineHighlight = new JButton(config.getLineHighlight());

        GridLayout gridLayout = new GridLayout(0, 2);
        gridLayout.setVgap(20);
        grid = new JPanel(gridLayout);
        grid.add(fontSizeLabel);
        grid.add(fontInput);
        grid.add(speedLabel);
        grid.add(speedSlider);
        grid.add(themeLabel);
        grid.add(themeInput);
        grid.add(tabSizeLabel);
        grid.add(tabSizeSlider);
        grid.add(autoSaveLabel);
        grid.add(autoSaveButton);
        grid.add(lineHighlightLabel);
        grid.add(lineHighlight);

        save = new JButton(SAVE);
        resetDefaults = new JButton(RESET);

        grid.add(save);
        grid.add(resetDefaults);

        resetDefaults.addActionListener(buttonActionListener);
        save.addActionListener(buttonActionListener);
        lineHighlight.addActionListener(buttonActionListener);

        popup.add(grid, BorderLayout.CENTER);

        popup.validate();
        popup.pack();
        popup.setVisible(true);
        this.applyTheme(new Font(Config.DEFAULT_FONT, Font.PLAIN, config.getFontSize()),
                EditorTheme.getTheme(config.getTheme()));
    }

    /**
     * Action listener helper class for settings operations buttons.
     */
    private static class ButtonActionListener implements ActionListener {

        public ButtonActionListener() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();
            SettingsPopup instance = SettingsPopup.getInstance();
            if (action.startsWith("Save")) {
                try {
                    instance.config.setFontSize(Integer.parseInt(instance.fontInput.getText()));
                } catch (NumberFormatException er) {
                    JOptionPane.showMessageDialog(new JFrame(), "Bad format for font size, please input a number");
                    return;
                }
                if (instance.autoSaveButton.getSliderValue() == 0) {
                    instance.config.setAutoSaveInterval(1);
                    instance.autoSaveButton.setToggleButtonStatus(false);
                }
                instance.config.setSimSpeed(instance.speedSlider.getValue());
                instance.config.setTabSize(instance.tabSizeSlider.getValue());
                instance.config.setTheme(instance.themeInput.getSelectedItem().toString());
                instance.config.setAutoSaveInterval(instance.autoSaveButton.getSliderValue());
                instance.config.setAutoSaveSelected(instance.autoSaveButton.getToggleButtonStatus());
                instance.config.setLineHighlight(instance.lineHighlight.getText());
                instance.config.saveChanges();
                instance.applyTheme(new Font(Config.DEFAULT_FONT, Font.PLAIN, instance.config.getFontSize()),
                        EditorTheme.getTheme(instance.config.getTheme()));
                Window.getInstance().applyConfiguration(instance.config);
            }
            if (action.startsWith("Reset")) {
                instance.config.resetDefaults();
                instance.fontInput.setText(Config.DEFAULT_FONT_SIZE);
                instance.speedSlider.setValue(Integer.parseInt(Config.DEFAULT_SIMULATION_SPEED));
                instance.tabSizeSlider.setValue(Integer.parseInt(Config.DEFAULT_TAB_SIZE));
                instance.themeInput.setSelectedIndex(0);
                instance.autoSaveButton.setToggleButtonStatus(Boolean.parseBoolean(Config.DEFAULT_AUTO_SAVE_SELECTED));
                instance.autoSaveButton.setSliderValue(Integer.parseInt(Config.DEFAULT_AUTO_SAVE_INTERVAL));
                instance.lineHighlight.setText(Config.DEFAULT_LINEHIGHLIGHT);
            }
            if (action.startsWith("Line") || action.startsWith("OFF")) {
                String option = instance.lineHighlight.getText();
                int position = instance.LineHighlightOption.indexOf(option);
                if (position == 2) {
                    position = 0;
                } else {
                    position += 1;
                }
                option = instance.LineHighlightOption.get(position);
                System.out.println(option);
                instance.lineHighlight.setText(option);
            }
        }
    }
}
