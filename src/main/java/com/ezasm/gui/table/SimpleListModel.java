package com.ezasm.gui.table;

import javax.swing.*;

/**
 * A simple list model based on a given array of any type. getElementAt uses toString on the given object.
 */
public class SimpleListModel extends AbstractListModel<Object> {

    private final Object[] elements;

    /**
     * Constructs a basic list model given an array of elements.
     *
     * @param elements the array of elements to base the model on.
     */
    public SimpleListModel(Object[] elements) {
        this.elements = elements;
    }

    /**
     * Gets the length of the underlying array.
     *
     * @return the length of the underlying array.
     */
    @Override
    public int getSize() {
        return elements.length;
    }

    /**
     * Gets the toString value of an item at a certain index.
     *
     * @param index the requested index.
     * @return the toString value of the selected item.
     */
    @Override
    public Object getElementAt(int index) {
        return elements[index].toString();
    }
}
