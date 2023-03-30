package com.ezasm.gui.util.spinner;

import javax.swing.*;

/**
 * A spinner model used to represent values of type long.
 */
public class SpinnerIntegerModel extends SpinnerNumberModel {

    private Integer value, stepSize;
    private Comparable<Integer> minimum, maximum;

    public SpinnerIntegerModel(Integer value, Integer minimum, Integer maximum, Integer stepSize) {
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.stepSize = stepSize;
    }

    @Override
    public Number getNumber() {
        return value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (int) value;
        fireStateChanged();
    }

    @Override
    public Object getNextValue() {
        int v = value + stepSize;
        return boundWithinRange(v);
    }

    @Override
    public Object getPreviousValue() {
        int v = value - stepSize;
        return boundWithinRange(v);
    }

    @Override
    public Integer getStepSize() {
        return stepSize;
    }

    @Override
    public Comparable<Integer> getMinimum() {
        return minimum;
    }

    @Override
    public Comparable<Integer> getMaximum() {
        return maximum;
    }

    private Object boundWithinRange(int value) {
        if ((maximum != null) && (maximum.compareTo(value) < 0)) {
            return null;
        }
        if ((minimum != null) && (minimum.compareTo(value) > 0)) {
            return null;
        }
        return value;
    }

}
