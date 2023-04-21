package com.ezasm.gui.util;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EzSettingsTab extends JPanel implements IThemeable {
	private static final Insets INSETS_5 = new Insets(5, 5, 5, 5);
	private static final JPanel EMPTY_PANEL = new JPanel();

	private int rowCount;

	public EzSettingsTab() {
		super(new GridBagLayout());
	}

	public void addMenuRow(final JLabel label, final Component component) {
		final var c = new GridBagConstraints();
		c.insets = INSETS_5;
		c.gridx = 0;
		c.gridy = rowCount;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(label, c);

		c.gridx = 1;
		add(component, c);

		++rowCount;
	}

	public void addVerticallyStretchedEmptyPanel() {
		final var c = new GridBagConstraints();
		c.gridy = rowCount;
		c.gridwidth = 2;
		c.weighty = 1;
		c.fill = GridBagConstraints.VERTICAL;
		add(EMPTY_PANEL, c);
	}

	@Override
	public void applyTheme(final Font font, final EditorTheme editorTheme) {
		final var foreground = editorTheme.foreground();
		final var background = editorTheme.background();

		final var border = BorderFactory.createMatteBorder(1, 1, 1, 1, foreground);
        final var buttonBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, foreground);

		setFont(font);
		setBackground(background);

		for (final var component : getComponents()) {
			// apply background color to all panels
			component.setFont(font);
			component.setBackground(background);

			if (component instanceof final JTextField tf) {
				tf.setCaretColor(foreground);
				EditorTheme.applyFontThemeBorder(tf, font, editorTheme, border);
			}
			
			if (component instanceof final JLabel l) {
				l.setOpaque(true);
			}

			if (component instanceof final JButton b) {
				EditorTheme.applyFontThemeBorder(b, font, editorTheme, buttonBorder);
			}
		}
	}
}
