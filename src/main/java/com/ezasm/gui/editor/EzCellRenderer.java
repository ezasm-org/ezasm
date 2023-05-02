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

    @Override
    protected void prepareForOtherCompletion(JList<?> list, Completion c, int index, boolean selected,
            boolean hasFocus) {
        super.prepareForOtherCompletion(list, c, index, selected, hasFocus);
        setIcon(getEmptyIcon());
    }

    @Override
    protected void prepareForVariableCompletion(JList<?> list, VariableCompletion vc, int index, boolean selected,
            boolean hasFocus) {
        super.prepareForVariableCompletion(list, vc, index, selected, hasFocus);
        setIcon(variableIcon);
    }

    @Override
    protected void prepareForFunctionCompletion(JList<?> list, FunctionCompletion fc, int index, boolean selected,
            boolean hasFocus) {
        super.prepareForFunctionCompletion(list, fc, index, selected, hasFocus);
        setIcon(functionIcon);
    }

}
