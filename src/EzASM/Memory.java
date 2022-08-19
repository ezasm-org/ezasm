package EzASM;

import java.util.Arrays;

public class Memory {

    public static final int DEFAULT_MEMORY_SIZE = 0x1_0000;
    public static final int DEFAULT_WORD_SIZE = 8;
    private static final int OFFSET = 0x1_0000;

    public final int WORD_SIZE;
    private final int MEMORY_SIZE;
    private final byte[] memory;
    private int alloc;

    public Memory() {
        this.WORD_SIZE = DEFAULT_WORD_SIZE;
        this.MEMORY_SIZE = DEFAULT_MEMORY_SIZE * WORD_SIZE;
        this.memory = new byte[MEMORY_SIZE];
        this.alloc = 0;
    }

    public Memory(int wordSize, int memorySize) {
        this.WORD_SIZE = wordSize;
        this.MEMORY_SIZE = memorySize * this.WORD_SIZE;
        this.memory = new byte[this.MEMORY_SIZE];
        this.alloc = 0;
    }

    public void reset() {
        Arrays.fill(memory, (byte) 0);
        alloc = 0;
    }

    public int size() {
        return MEMORY_SIZE;
    }

    public int initialStackPointer() {
        return MEMORY_SIZE + OFFSET;
    }

    public int initialHeapPointer() {
        return 0 + OFFSET;
    }

    public int currentHeapPointer() {
        return alloc + OFFSET;
    }

    // Allocate with check based on stack pointer
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

    // Allocate without check based on stack pointer
    public int allocate(int bytes) {
        int addr = alloc;
        alloc = alloc + bytes;
        return addr + OFFSET;
    }

    public byte[] read(int address, int count) {
        address = address - OFFSET;
        if(address < 0 || (address + count) >= this.MEMORY_SIZE) {
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
        if(address < 0 || (address + maxSize) >= this.MEMORY_SIZE) {
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
        if(address < 0 || (address + data.length) >= this.MEMORY_SIZE) {
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
            return;
        }
        if(address < 0 || (address + data.getBytes().length) >= this.MEMORY_SIZE) {
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
