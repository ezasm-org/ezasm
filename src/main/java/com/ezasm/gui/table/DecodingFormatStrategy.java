package com.ezasm.gui.table;

import com.ezasm.simulation.Memory;
import com.ezasm.simulation.exception.ReadOutOfBoundsException;
import com.ezasm.util.RawData;
import java.nio.*;

public class DecodingFormatStrategy implements MemoryFormatStrategy {

    private int displaySize = Memory.getWordSize();
    private final String INT = "Int";
    private final String FLOAT = "Float";
    private final String ASCII = "Ascii";
    private String mode = ASCII;

    /**
     * Sets the decoding mode for this strategy. The mode determines how raw memory values are interpreted and displayed
     * (e.g., as ints, floats, ASCII characters).
     *
     * @param mode the decoding mode to apply.
     */
    public void setMode(String mode) {
        this.mode = mode;
        if (mode.equals(ASCII)) {
            displaySize = 1;
        } else {
            displaySize = Memory.getWordSize();
        }
    }

    /**
     * Returns the display size for this format strategy in bytes.
     *
     * @return the number of bytes each displayed value occupies.
     */
    @Override
    public int getDisplaySize() {
        return displaySize;
    }

    /**
     * Gets the memory display string for a specified cell.
     *
     * @param memory the memory table to get values from
     * @param row    the row of the cell in question.
     * @param cols   the number of columns in the table
     * @param col    the column of the cell in question.
     * @param offset the offset from within the memory at which the memory table will display
     * @return the value of memory that the word stored represents.
     */
    @Override
    public Object getValueAt(Memory memory, int row, int cols, int col, int offset) {
        try {
            byte[] curWord = memory.read(offset + (row * cols + col) * displaySize).data();
            switch (mode) {
            case ASCII -> {
                return (char) curWord[3];
            }
            case FLOAT -> {
                return ByteBuffer.wrap(curWord).order(ByteOrder.BIG_ENDIAN).getFloat();
            }
            case INT -> {
                return ByteBuffer.wrap(curWord).order(ByteOrder.BIG_ENDIAN).getInt();
            }
            default -> {
                return memory.read(offset + (row * cols + col) * displaySize).toHexString();
            }
            }
        } catch (ReadOutOfBoundsException e) {
            return RawData.emptyBytes(displaySize).toHexString();
        }
    }

    /**
     * Gets the name of the column to be used in a header.
     *
     * @param column the column number to find the name of.
     * @return the name of the given column.
     */
    @Override
    public String getColumnName(int column) {
        return "+" + Long.toHexString((long) column * displaySize);
    }
}
