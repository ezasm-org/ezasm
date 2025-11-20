package com.ezasm.gui.editor;

import javax.swing.Icon;
import javax.swing.JList;

import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionCellRenderer;
import org.fife.ui.autocomplete.FunctionCompletion;
import org.fife.ui.autocomplete.VariableCompletion;

/**
 * The cell renderer for the EzASM programming language. Currently unused. For future autocomplete development.
 */
public class EzCellRenderer extends CompletionCellRenderer {

    private final Icon variableIcon;
    private final Icon functionIcon;

    /**
     * Constructs the cell renderer.
     */
    public EzCellRenderer() {
        variableIcon = getIcon("img/var.png");
        functionIcon = getIcon("img/function.png");
    }

    /**
     * Prepares the cell renderer for a generic completion item that is neither a variable nor a function.
     * Sets the cell icon to an empty icon, resulting in no icon being displayed for generic completions.
     *
     * @param list      the JList displaying the completions.
     * @param c         the completion object to be rendered.
     * @param index     the index of the item in the list.
     * @param selected  true if the item is selected, false otherwise.
     * @param hasFocus  true if the item has focus, false otherwise.
     */
    @Override
    protected void prepareForOtherCompletion(JList<?> list, Completion c, int index, boolean selected,
            boolean hasFocus) {
        super.prepareForOtherCompletion(list, c, index, selected, hasFocus);
        setIcon(getEmptyIcon());
    }

    /**
     * Prepares the cell renderer for a variable completion item.
     * Sets the cell icon to the variable icon (img/var.png), which is displayed alongside the variable name.
     *
     * @param list      the JList displaying the completions.
     * @param vc        the variable completion object to be rendered.
     * @param index     the index of the variable in the list.
     * @param selected  true if the variable is selected, false otherwise.
     * @param hasFocus  true if the variable has focus, false otherwise.
     */
    @Override
    protected void prepareForVariableCompletion(JList<?> list, VariableCompletion vc, int index, boolean selected,
            boolean hasFocus) {
        super.prepareForVariableCompletion(list, vc, index, selected, hasFocus);
        setIcon(variableIcon);
    }

    /**
     * Prepares the cell renderer for a function completion item.
     * Sets the cell icon to the function icon (img/function.png), which is displayed alongside the function name.
     *
     * @param list      the JList displaying the completions.
     * @param fc        the function completion object to be rendered.
     * @param index     the index of the function in the list.
     * @param selected  true if the function is selected, false otherwise.
     * @param hasFocus  true if the function has focus, false otherwise.
     */
    @Override
    protected void prepareForFunctionCompletion(JList<?> list, FunctionCompletion fc, int index, boolean selected,
            boolean hasFocus) {
        super.prepareForFunctionCompletion(list, fc, index, selected, hasFocus);
        setIcon(functionIcon);
    }

}
