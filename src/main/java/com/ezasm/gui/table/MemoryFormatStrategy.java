package com.ezasm.gui.table;

import com.ezasm.simulation.Memory;

public interface MemoryFormatStrategy {
    int getDisplaySize();

    Object getValueAt(Memory memory, int row, int cols, int col, int offset);

    String getColumnName(int col);

    default void setMode(String mode) {
    };
}
