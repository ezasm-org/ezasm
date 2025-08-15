package com.ezasm.gui.table;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.exception.ReadOutOfBoundsException;
import com.ezasm.util.RawData;
import java.nio.*;

public class DecodingFormatStrategy implements MemoryFormatStrategy {

    private int displaySize = Memory.getWordSize();
    private String INT = "Int";
    private String FLOAT = "Float";
    private String ASCII = "Ascii";
    private String mode = ASCII;

    public void setMode(String mode){
        this.mode = mode;
        if(mode.equals(ASCII)){
            displaySize = 1;
        }else{
            displaySize = Memory.getWordSize();
        }
    }

    @Override
    public int getDisplaySize(){
        return displaySize;
    }

    @Override
    public Object getValueAt(Memory memory, int row, int cols, int col, int offset){
        try {
            byte[] curWord = memory.read(offset + (row * cols + col) * displaySize).data();
            if(mode.equals(ASCII)) {
                return (char) curWord[3];
            }else if(mode.equals(FLOAT)){
                float val = ByteBuffer.wrap(curWord).order(ByteOrder.BIG_ENDIAN).getFloat();
                return val;
            }else if(mode.equals(INT)){
                int val = ByteBuffer.wrap(curWord).order(ByteOrder.BIG_ENDIAN).getInt();
                return val;
            }else{
                return memory.read(offset + (row * cols + col) * displaySize).toHexString();
            }
        } catch (ReadOutOfBoundsException e) {
            return RawData.emptyBytes(displaySize).toHexString();
        }
    }

    @Override
    public String getColumnName(int column) {
        return "+" + Long.toHexString((long) column * displaySize);
    }
}
