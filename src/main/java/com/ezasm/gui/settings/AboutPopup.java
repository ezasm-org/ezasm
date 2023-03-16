package com.ezasm.gui.settings;

import com.ezasm.gui.Window;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;
import com.ezasm.util.Properties;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.time.Year;

/**
 * A singleton popup to display basic information about the program.
 */
public class AboutPopup implements IThemeable {

    private static AboutPopup instance;

    private final Config config;

    private final JFrame popup;
    private JEditorPane textPane;

    protected AboutPopup() {
        instance = this;
        popup = new JFrame("About");
        config = Window.getInstance().getConfig();
        initialize();
    }

    public static void openPopup() {
        if (instance == null) {
            new AboutPopup();
        } else if (!instance.popup.isVisible()) {
            instance.popup.setVisible(true);
        }
    }

    public void applyTheme(Font font, Theme theme) {
        Theme.applyFontAndTheme(textPane, font, theme);
    }

    private void initialize() {
        BorderLayout layout = new BorderLayout();
        popup.setLayout(layout);
        popup.setMinimumSize(new Dimension(200, 100));
        popup.setResizable(true);
        popup.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        String foregroundColor = Integer.toHexString(Theme.getTheme(config.getTheme()).foreground().getRGB());
        String linkColor = Integer.toHexString(Theme.getTheme(config.getTheme()).cyan().getRGB());

        // An interesting way to display this information, but it is a way to get configurable text and clickable links.
        // Uses HTML and registering a hyperlink listener for on-click events to display information and links.
        String text = String.format("""
                <html>
                    <p>%s</p>
                    <p>%s</p>
                    <p>Version: %s</p>
                    <p>User manual:
                        <a href="https://github.com/ezasm-org/EzASM/wiki">https://github.com/ezasm-org/EzASM/wiki</a>
                    </p>
                    <p>README and Source code:
                        <a href="https://github.com/ezasm-org/EzASM">https://github.com/ezasm-org/EzASM</a>
                    </p>
                    <p>Source code and JAR license:
                        <a href="https://github.com/ezasm-org/EzASM/blob/main/LICENSE">MIT License</a>
                    </p>
                    <p>Binary executable (.exe, .zip, etc.) license:
                        <a href="https://github.com/ezasm-org/EzASM-releases/blob/main/LICENSE">GPLv3 License</a>
                    </p>
                    <p>Copyright (c) 2022-%s Trevor Brunette</p>
                </html>
                """, Properties.NAME, Properties.DESCRIPTION, Properties.VERSION, Year.now().getValue());
        textPane = new JEditorPane();

        HTMLEditorKit kit = new HTMLEditorKit();
        textPane.setEditorKit(kit);
        StyleSheet style = kit.getStyleSheet();
        // Set the internal CSS for the editor pane
        style.addRule(String.format("p {color:#%s; font-size:%dpx;}", foregroundColor, config.getFontSize()));
        style.addRule(String.format("a {color:#%s; font-size:%dpx;}", linkColor, config.getFontSize()));

        textPane.setText(text);
        textPane.setBorder(padding);
        textPane.setOpaque(true);
        textPane.setEditable(false);

        textPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                // On certain operating systems (Linux) where the browser is wrapped with a verbose execution,
                // this writes to System.out with output from the browser program. Well, not the actual System.out
                // stream, but the output referred to by FD 1 (and maybe FD 2).
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (Exception ignored) {
                }
            }
        });

        popup.setContentPane(textPane);

        popup.validate();
        popup.pack();
        popup.setVisible(true);

        // This is kind of a hack tbh. Needs something better.
        // Needs a better way to get the config honestly. Same thing happens in the settings popup.
        applyTheme(new Font(Config.DEFAULT_FONT, Font.PLAIN, config.getFontSize()), Theme.getTheme(config.getTheme()));
    }

}
