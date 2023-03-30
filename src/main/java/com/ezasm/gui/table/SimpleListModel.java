package com.ezasm.gui.table;

import javax.swing.*;

public class SimpleListModel extends AbstractListModel<Object> {

    private final Object[] elements;

    public SimpleListModel(Object[] elements) {
        this.elements = elements;
    }

    @Override
    public int getSize() {
        return elements.length;
    }

    @Override
    public Object getElementAt(int index) {
        return elements[index].toString();
    }
}
