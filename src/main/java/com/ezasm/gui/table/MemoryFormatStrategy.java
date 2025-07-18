package com.ezasm.gui.table;
import com.ezasm.simulation.Memory;

public interface MemoryFormatStrategy {
    Object getValueAt(Memory memory, int row, int cols, int col, int offset);
    String getColumnName(int col);

}
