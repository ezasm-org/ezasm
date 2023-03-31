package com.ezasm.gui.util.spinner;

import javax.swing.*;

/**
 * A spinner model used to represent values of type int.
 */
public class SpinnerIntegerModel extends SpinnerNumberModel {

    private Integer value, stepSize;
    private Comparable<Integer> minimum, maximum;

    /**
     * Constructs a spinner model representation given an initial value, minimum, maximum, and step size.
     *
     * @param value    the initial value.
     * @param minimum  the minimum value.
     * @param maximum  the maximum value.
     * @param stepSize the step size.
     */
    public SpinnerIntegerModel(Integer value, Integer minimum, Integer maximum, Integer stepSize) {
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.stepSize = stepSize;
    }

    /**
     * Gets the number corresponding to the current value.
     *
     * @return the number corresponding to the current value.
     */
    @Override
    public Number getNumber() {
        return value;
    }

    /**
     * Gets the object form corresponding to the current value.
     *
     * @return the object form corresponding to the current value.
     */
    @Override
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value to the given integer auto-box compatible type.
     *
     * @param value the current <code>Number</code> for this sequence.
     */
    @Override
    public void setValue(Object value) {
        this.value = (int) value;
        fireStateChanged();
    }

    /**
     * Gets the value one step larger than the current value.
     *
     * @return the value one step larger than the current value.
     */
    @Override
    public Object getNextValue() {
        int v = value + stepSize;
        return boundWithinRange(v);
    }

    /**
     * Gets the value one step smaller than the current value.
     *
     * @return the value one step smaller than the current value.
     */
    @Override
    public Object getPreviousValue() {
        int v = value - stepSize;
        return boundWithinRange(v);
    }

    /**
     * Gets the step size used.
     *
     * @return the step size used.
     */
    @Override
    public Integer getStepSize() {
        return stepSize;
    }

    /**
     * Gets the minimum value.
     *
     * @return the minimum value.
     */
    @Override
    public Comparable<Integer> getMinimum() {
        return minimum;
    }

    /**
     * Gets the maximum value.
     *
     * @return the maximum value.
     */
    @Override
    public Comparable<Integer> getMaximum() {
        return maximum;
    }

    /**
     * Ensures that the given value is bound within the range of [minimum, maximum]
     *
     * @param value the value to check.
     * @return the value as an object if it is within the bounds, null otherwise.
     */
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
