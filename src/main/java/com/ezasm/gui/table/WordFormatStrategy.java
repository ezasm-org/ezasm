package com.ezasm.gui.table;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.exception.ReadOutOfBoundsException;
import com.ezasm.util.RawData;

public class WordFormatStrategy implements MemoryFormatStrategy {

    @Override
    public Object getValueAt(Memory memory, int row, int cols, int col, int offset){
        try {
            return memory.read(offset + (row * cols + col) * Memory.getWordSize()).toHexString();
        } catch (ReadOutOfBoundsException e) {
            return RawData.emptyBytes(Memory.getWordSize()).toHexString();
        }
    }

    @Override
    public String getColumnName(int column) {
        return "+" + Long.toHexString((long) column * Memory.getWordSize());
    }
}
