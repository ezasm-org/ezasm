package com.ezasm.gui.settings;

import com.ezasm.gui.Window;
import com.ezasm.gui.util.EditorTheme;
import com.ezasm.util.MavenProperties;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Year;

/**
 * A popup factory to display a GUI element with basic information about the program.
 */
public class AboutPopup {

    private static JFrame popup;

    /**
     * Opens an About popup if one is not already open.
     */
    public static void openPopup() {
        if (popup == null) {
            popup = new JFrame("About");
            initialize();
        }
    }

    /**
     * Sets up the content of the popup.
     */
    private static void initialize() {
        BorderLayout layout = new BorderLayout();
        popup.setLayout(layout);
        popup.setMinimumSize(new Dimension(200, 200));
        popup.setResizable(true);

        popup.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                popup.setVisible(false);
                popup.dispose();
                popup = null;
            }
        });
    }
        /**
         * Returns the About panel as a reusable component for tabs.
         *
         * @return a scrollable HTML panel showing about info
         */
        public static JComponent getAboutPanel() {
            Config config = Window.getInstance().getConfig();
            EditorTheme theme = config.getTheme();

            String text = String.format("""
            <html>
                <p>%s</p>
                <p>%s</p>
                <p>Version: %s</p>
                <p>User manual:
                    <a href="https://github.com/ezasm-org/EzASM/wiki">https://github.com/ezasm-org/EzASM/wiki</a>
                </p>
                <p>Source code and README:
                    <a href="https://github.com/ezasm-org/EzASM">https://github.com/ezasm-org/EzASM</a>
                </p>
                <p>License for source code and JAR:
                    <a href="https://github.com/ezasm-org/EzASM/blob/main/LICENSE">MIT License</a>
                </p>
                <p>License for binary executables (.exe, .zip, etc.):
                    <a href="https://github.com/ezasm-org/EzASM-releases/blob/main/LICENSE">GPLv3 License</a>
                </p>
                <p>Copyright (c) 2022-%s Trevor Brunette</p>
            </html>
            """, MavenProperties.NAME, MavenProperties.DESCRIPTION, MavenProperties.VERSION, Year.now().getValue());

            JEditorPane textPane = new JEditorPane();
            HTMLEditorKit kit = new HTMLEditorKit();
            textPane.setEditorKit(kit);

            StyleSheet style = kit.getStyleSheet();
            style.addRule(String.format("p {color:#%s;}", colorCodeHex(theme.foreground())));
            style.addRule(String.format("a {color:#%s;}", colorCodeHex(theme.cyan())));
            style.addRule(String.format("html {background-color: #%s;}", colorCodeHex(theme.background())));
            style.setBaseFontSize(config.getFontSize());

            textPane.setText(text);
            textPane.setBackground(theme.background());
            textPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            textPane.setOpaque(true);
            textPane.setEditable(false);

            textPane.addHyperlinkListener(e -> {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (Exception ignored) {}
                }
            });

            JScrollPane scrollPane = new JScrollPane(textPane);
            scrollPane.setBorder(null);
            return scrollPane;
        }
    /**
     * Converts a {@link Color} object to a 6-digit hexadecimal color code.
     *
     * @param color the color to convert
     * @return the 6-digit hex string (e.g., "ffffff" for white)
     */
    private static String colorCodeHex(Color color) {
        return String.format("%06x", color.getRGB() & 0xFFFFFF);
    }

}
