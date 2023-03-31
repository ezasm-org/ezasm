package com.ezasm;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
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

    private static class VBoxPanel extends JPanel {
        private VBoxPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }
    }

    private static class HBoxPanel extends JPanel {
        private HBoxPanel() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        }
    }

    private static class LeftFlowPanel extends JPanel {
        private LeftFlowPanel() {
            super(new FlowLayout(FlowLayout.LEFT));
        }

        private LeftFlowPanel(int hgap, int vgap) {
            super(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        }
    }

    private static class RightFlowPanel extends JPanel {
        private RightFlowPanel() {
            super(new FlowLayout(FlowLayout.RIGHT));
        }

        private RightFlowPanel(int hgap, int vgap) {
            super(new FlowLayout(FlowLayout.RIGHT, hgap, vgap));
        }
    }

    private static HBoxPanel settingsRow(JLabel label, Component component) {
        final var panel = new HBoxPanel();

        final var labelPanel = new LeftFlowPanel();
        final var componentPanel = new RightFlowPanel();

        labelPanel.add(label);
        componentPanel.add(component);

        panel.add(labelPanel);
        panel.add(componentPanel);

        return panel;
    }

    private static RightFlowPanel buttonsRow(JButton... buttons) {
        final var row = new RightFlowPanel(5, 5);
        for (final var button : buttons)
            row.add(button);
        return row;
    }

    static {
        // disable button actions for now, this is a visual test
        // buttonActionListener = new ButtonActionListener();
        final var popup = new JFrame("EzASM Configurator");
        popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final var themeLabel = new JLabel("Theme");
        final var themeInput = new JComboBox<>(new String[] { "theme 1", "theme 2" });
        themeInput.setSelectedItem("test1");

        final var fontSizeLabel = new JLabel("Font Size");
        final var speedLabel = new JLabel("Instruction Delay");

        final var fontInput = new JTextField("font input");
        final var speedSlider = new JSlider(10, 1000, 69);

        // constructing the "General" tab
        final var generalPanel = new VBoxPanel();
        final var padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        generalPanel.setBorder(padding);

        final var fontSizeRow = settingsRow(fontSizeLabel, fontInput);
        generalPanel.add(fontSizeRow);

        final var speedRow = settingsRow(speedLabel, speedSlider);
        generalPanel.add(speedRow);

        final var themePanel = settingsRow(themeLabel, themeInput);
        generalPanel.add(themePanel);

        final var save = new JButton("Save Changes");
        final var resetDefaults = new JButton("Reset to Defaults");

        final var buttonsRow = buttonsRow(save, resetDefaults);
        buttonsRow.setBounds(100, 100, 100, 100);
        generalPanel.add(buttonsRow);

        final var tp = new JTabbedPane(JTabbedPane.LEFT);
        tp.setBounds(0, 0, popup.getWidth(), popup.getHeight());

        // adding the tabs to a JTabbedPane
        tp.add("General", generalPanel);

        // disable button actions for now, this is a visual test
        // resetDefaults.addActionListener(buttonActionListener);
        // save.addActionListener(buttonActionListener);

        popup.add(tp);

        popup.validate();
        popup.pack();
        popup.setVisible(true);
    }

    public static void main(String[] args) {
    }

}
