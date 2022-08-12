package EzASM;

import java.util.Arrays;

public class Memory {

    public static final int WORD_SIZE = 8;

    private static final int DEFAULT_SIZE = 0x1_0000;
    private static final int OFFSET = 0x1_0000;

    private final int size;
    private final byte[] memory;
    private int alloc;

    public Memory() {
        this.size = DEFAULT_SIZE * WORD_SIZE;
        this.memory = new byte[size];
        this.alloc = 0;
    }

    public Memory(int size) {
        this.size = size * WORD_SIZE;
        this.memory = new byte[this.size];
        this.alloc = 0;
    }

    public int size() {
        return size;
    }

    public int initialStackPointer() {
        return size + OFFSET;
    }

    public int initialHeapPointer() {
        return 0 + OFFSET;
    }

    public int currentHeapPointer() {
        return alloc + OFFSET;
    }

    public int allocate(int bytes, int sp) {
        if(alloc + bytes + OFFSET > sp) {
            // Error: Attempted to allocate onto the stack
            System.out.println("Error allocating");
            return 0;
        }
        int addr = alloc;
        alloc = alloc + bytes;
        return addr + OFFSET;
    }

    public int allocate(int bytes) {
        int addr = alloc;
        alloc = alloc + bytes;
        return addr + OFFSET;
    }

    public byte[] read(int address, int count) {
        address = address - OFFSET;
        if(address < 0 || (address + count) >= this.size) {
            // Error: address is out of bounds
            System.out.println("Error: possible read out of bounds with address" + address);
            return null;
        }

        return Arrays.copyOfRange(memory, address, address + count);
    }

    public long readLong(int address) {
        return Conversion.bytesToLong(read(address, WORD_SIZE));
    }

    public String readString(int address, int maxSize) {
        address = address - OFFSET;
        if(maxSize < 0) {
            System.out.println("Error: max string size cannot be less than zero");
        }
        if(address < 0 || (address + maxSize) >= this.size) {
            // Error: address is out of bounds
            System.out.println("Error: possible read out of bounds with address " + address);
            return null;
        }

        byte[] toString = new byte[maxSize];
        for(int i = 0; i < maxSize; ++i) {
            toString[i] = memory[address + i];
            if(memory[address + i] == '\0') {
                break;
            }
        }

        return Conversion.bytesToString(toString);
    }

    public void write(int address, byte[] data) {
        address = address - OFFSET;
        if(address < 0 || (address + data.length) >= this.size) {
            // Error: address is out of bounds
            System.out.println("Error: address is out of bounds");
            return;
        }

        System.arraycopy(data, 0, memory, address, data.length);
    }

    public void writeLong(int address, long data) {
        write(address, Conversion.longToBytes(data));
    }

    public void writeString(int address, String data, int maxSize) {
        address = address - OFFSET;
        if(maxSize < 0) {
            System.out.println("Error: max string size cannot be less than zero");
        }
        if(address < 0 || (address + data.getBytes().length) >= this.size) {
            // Error: address is out of bounds
            System.out.println("Error: address is out of bounds");
            return;
        }

        for(int i = 0; i < data.getBytes().length && i < maxSize; ++i) {
            memory[address + i] = data.getBytes()[i];
        }

        if(maxSize <= data.getBytes().length) {
            memory[address + maxSize] = '\0';
        }
    }

}
