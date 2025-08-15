package com.ezasm.gui.table;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.exception.ReadOutOfBoundsException;
import com.ezasm.util.RawData;

public class WordFormatStrategy implements MemoryFormatStrategy {

    int displaySize = Memory.getWordSize();

    /**
     * Returns the display size for this format strategy in bytes.
     *
     * @return the number of bytes each displayed value occupies.
     */
    @Override
    public int getDisplaySize(){
        return displaySize;
    }

    /**
     * Gets the memory display string for a specified cell.
     *
     * @param memory the memory table to get values from
     * @param row the row of the cell in question.
     * @param cols the number of columns in the table
     * @param col the column of the cell in question.
     * @param offset the offset from within the memory at which the memory table will display
     * @return a hex string of the data contained in the corresponding memory.
     */
    @Override
    public Object getValueAt(Memory memory, int row, int cols, int col, int offset){
        try {
            return memory.read(offset + (row * cols + col) * displaySize).toHexString();
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
