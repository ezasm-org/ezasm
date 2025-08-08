package com.ezasm.gui.table;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.exception.ReadOutOfBoundsException;
import com.ezasm.util.RawData;

public class DecodingFormatStrategy implements MemoryFormatStrategy {

    boolean littleEndian = false;
    int displaySize = 1;

    @Override
    public int getDisplaySize(){
        return displaySize;
    }

    @Override
    public Object getValueAt(Memory memory, int row, int cols, int col, int offset) {
        try {
            byte[] curWord = memory.read(offset + (row * cols + col), 1).data();
            return (char)curWord[0];
        } catch (ReadOutOfBoundsException e) {
            return RawData.emptyBytes(Memory.getWordSize()).toHexString();
        }
    }

    @Override
    public String getColumnName(int column) {
        return "+" + Long.toHexString((long) column);
    }
}

