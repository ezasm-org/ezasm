package com.ezasm.gui;

import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The editor pane within the GUI. Allows the user to type code or edit loaded code.
 */
public class EditorPane extends JPanel {

    private final JTextArea textArea;
    private final JTextArea lineNumbers;
    private static final Dimension MIN_SIZE = new Dimension(600, 400);
    private static final Dimension MAX_SIZE = new Dimension(600, 2000);

    /**
     * Creates a text edit field with an "Undo Manager" to undo the user's actions with
     * CTRL + Z or redo those undid actions with CTRL + SHIFT + Z or CTRL + Y.
     */
    public EditorPane() {
        super();
        lineNumbers = new JTextArea("1");
        lineNumbers.setBackground(Color.LIGHT_GRAY);
        textArea = new JTextArea();
        textArea.setEditable(true);
        textArea.setLineWrap(false);
        textArea.setMinimumSize(MIN_SIZE);
        textArea.setDisabledTextColor(Color.DARK_GRAY);
        UndoManager manager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(manager);
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public String getText(){
                Element root = textArea.getDocument().getDefaultRootElement();
                int length = textArea.getDocument().getLength();
                String result = "1\n";
                for(int i = 2; i <= root.getElementIndex(length)+1; i++){
                    result += i+"\n";
                }
                return result;
            }

            @Override
            public void changedUpdate(DocumentEvent e){
                lineNumbers.setText(getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e){
                lineNumbers.setText(getText());
            }

            @Override
            public void insertUpdate(DocumentEvent e){
                lineNumbers.setText(getText());
            }


        });
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int mod = keyEvent.getModifiersEx();
                if((mod & KeyEvent.CTRL_DOWN_MASK) == KeyEvent.CTRL_DOWN_MASK) {
                    // The "control" button is pressed
                    int key = keyEvent.getKeyCode();
                    if(key == KeyEvent.VK_Z) {
                        if((mod & KeyEvent.SHIFT_DOWN_MASK) == KeyEvent.SHIFT_DOWN_MASK) {
                            // ctrl + shift + z : redo
                            if(manager.canRedo()) {
                                manager.redo();
                            }
                        } else if(manager.canUndo()) {
                            // ctrl + z : undo
                            manager.undo();
                        }
                    } else if(key == KeyEvent.VK_Y) {
                        // ctrl + y : redo
                        if(manager.canRedo()) {
                            manager.redo();
                        }
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent keyEvent) {}
        });


        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setRowHeaderView(lineNumbers);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMinimumSize(textArea.getSize());
        scrollPane.setPreferredSize(textArea.getPreferredSize());
        setMaximumSize(MAX_SIZE);
        setLayout(new BorderLayout());
        add(scrollPane);
    }

    /**
     * Sets the editable state of the text field based on the boolean given.
     * @param value true to be editable, false to not be editable.
     */
    public void setEditable(boolean value) {
        textArea.setEnabled(value);
    }

    /**
     * Gets the truth value of whether the editor can be typed in.
     * @return true if the editor can be typed in currently, false otherwise.
     */
    public boolean getEditable() {
        return textArea.isEnabled();
    }

    /**
     * Gets the text content of the text editor.
     * @return the text content of the text editor.
     */
    public String getText() {
        return textArea.getText();
    }

    /**
     * Sets the text of the editor to the given content.
     * @param content the text to set the text within the editor to.
     */
    public void setText(String content) {
        textArea.setText(content);
    }
}
