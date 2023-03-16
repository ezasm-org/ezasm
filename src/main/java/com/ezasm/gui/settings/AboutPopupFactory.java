package com.ezasm.gui.settings;

import com.ezasm.gui.Window;
import com.ezasm.gui.util.Theme;
import com.ezasm.util.Properties;

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
public class AboutPopupFactory {

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
     * Sets up the content of the popup
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
                """, Properties.NAME, Properties.DESCRIPTION, Properties.VERSION, Year.now().getValue());

        Config config = Window.getInstance().getConfig();
        JEditorPane textPane = new JEditorPane();

        HTMLEditorKit kit = new HTMLEditorKit();
        textPane.setEditorKit(kit);
        StyleSheet style = kit.getStyleSheet();
        // Set the internal CSS for the editor pane
        Theme theme = Theme.getTheme(config.getTheme());
        style.addRule(String.format("p {color:#%s;}", colorCodeHex(theme.foreground())));
        style.addRule(String.format("a {color:#%s;}", colorCodeHex(theme.cyan())));
        style.addRule(String.format("html {background-color: #%s;}", colorCodeHex(theme.background())));
        style.setBaseFontSize(config.getFontSize());

        textPane.setBackground(theme.background());

        textPane.setText(text);
        textPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
    }

    private static String colorCodeHex(Color color) {
        return String.format("%06x", color.getRGB() & 0xFFFFFF);
    }

}
