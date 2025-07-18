package com.ezasm.gui.table;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.exception.ReadOutOfBoundsException;
import com.ezasm.util.RawData;

public class ByteFormatStrategy implements MemoryFormatStrategy {

    boolean littleEndian = true;

    @Override
    public Object getValueAt(Memory memory, int row, int cols, int col, int offset) {
        try {
            byte[] curWord = memory.read(offset + (row * cols + col), 1).data();
            return String.format("%02X", curWord[0]);
        } catch (ReadOutOfBoundsException e) {
            return RawData.emptyBytes(Memory.getWordSize()).toHexString();
        }
    }

    @Override
    public String getColumnName(int column) {
        return "+" + Long.toHexString((long) column);
    }
}

