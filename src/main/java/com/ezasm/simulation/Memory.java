package com.ezasm.simulation;

import com.ezasm.Conversion;

import java.util.Arrays;

/**
 * Represents the system memory. There will be a single and contiguous array of memory which represents both the stack
 * and heap with the stack growing downward and the heap growing upward. Implements an "offset" for the address spacing
 * to not start at 0. Keeps track of allocated memory and throws an exception when the heap crosses the stack. Has a
 * default size of 2^16 words (or 2^19 bytes). Has a default word size of 8 bytes (the typical long integer or long
 * float size).
 */
public class Memory {

    /**
     * The default number of words possible to store in the system.
     */
    public static final int DEFAULT_MEMORY_WORDS = 0x1_0000;

    /**
     * The default word size of the system.
     */
    public static final int DEFAULT_WORD_SIZE = 8;

    /**
     * The offset applied to all output addresses.
     */
    private static final int OFFSET = 0x1_0000;

    public final int WORD_SIZE;
    private final int MEMORY_SIZE;
    private final byte[] memory;
    private int alloc;

    /**
     * Constructs memory with the default parameters.
     */
    public Memory() {
        this.WORD_SIZE = DEFAULT_WORD_SIZE;
        this.MEMORY_SIZE = DEFAULT_MEMORY_WORDS * WORD_SIZE;
        this.memory = new byte[MEMORY_SIZE];
        this.alloc = 0;
    }

    /**
     * Constructs memory with different parameters for word size and memory size.
     *
     * @param wordSize   the word size in bytes.
     * @param memorySize the memory size in words.
     */
    public Memory(int wordSize, int memorySize) {
        this.WORD_SIZE = wordSize;
        this.MEMORY_SIZE = memorySize * this.WORD_SIZE;
        this.memory = new byte[this.MEMORY_SIZE];
        this.alloc = 0;
    }

    /**
     * Resets the memory by setting all values to zero and returning the allocation pointer to zero.
     */
    public void reset() {
        Arrays.fill(memory, (byte) 0);
        alloc = 0;
    }

    /**
     * Gets the size of the memory as a number of words.
     *
     * @return the size of the memory as a number of words.
     */
    public int size() {
        return MEMORY_SIZE;
    }

    /**
     * Gets the initial stack pointer of the memory.
     *
     * @return the initial stack pointer of the memory.
     */
    public int initialStackPointer() {
        return MEMORY_SIZE + OFFSET;
    }

    /**
     * Gets the initial heap pointer of the memory.
     *
     * @return the initial heap pointer of the memory.
     */
    public int initialHeapPointer() {
        return 0 + OFFSET;
    }

    /**
     * Gets the current heap pointer of the memory.
     *
     * @return the current heap pointer of the memory.
     */
    public int currentHeapPointer() {
        return alloc + OFFSET;
    }

    /**
     * Allocates a certain number of bytes with a check based on stack pointer to ensure that the heap pointer does not
     * cross the stack pointer.
     *
     * @param bytes the number of bytes to allocate.
     * @param sp    the current stack pointer of the program.
     * @return the allocated memory starting point
     */
    public int allocate(int bytes, int sp) {
        if (alloc + bytes + OFFSET > sp) {
            // Error: Attempted to allocate onto the stack
            System.out.println("Error allocating");
            return 0;
        }
        int addr = alloc;
        alloc = alloc + bytes;
        return addr + OFFSET;
    }

    /**
     * Allocates a certain number of bytes without a check based on stack pointer.
     *
     * @param bytes the number of bytes to allocate.
     * @return the allocated memory starting point
     */
    public int allocate(int bytes) {
        int addr = alloc;
        alloc = alloc + bytes;
        return addr + OFFSET;
    }

    /**
     * Gets the information from the memory at a certain address.
     *
     * @param address the address to begin to read from.
     * @param count   the number of bytes to read.
     * @return the information read from the memory at a certain address.
     */
    public byte[] read(int address, int count) {
        address = address - OFFSET;
        if (address < 0 || (address + count) > this.MEMORY_SIZE) {
            // Error: address is out of bounds
            System.out.println("Error: possible read out of bounds with address" + address);
            return null;
        }

        return Arrays.copyOfRange(memory, address, address + count);
    }

    /**
     * Gets the information from the memory at a certain address interpreted as a long.
     *
     * @param address the address to begin to read from.
     * @return the long read from the memory at a certain address.
     */
    public long readLong(int address) {
        return Conversion.bytesToLong(read(address, WORD_SIZE));
    }

    /**
     * Gets the information from the memory at a certain address interpreted as a String.
     *
     * @param address the starting address to read from.
     * @param maxSize the maximum size of the String to be in bytes.
     * @return the String interpreted.
     */
    public String readString(int address, int maxSize) {
        address = address - OFFSET;
        if (maxSize < 0) {
            System.out.println("Error: max string size cannot be less than zero");
        }
        if (address < 0 || (address + maxSize) >= this.MEMORY_SIZE) {
            // Error: address is out of bounds
            System.out.println("Error: possible read out of bounds with address " + address);
            return null;
        }

        byte[] toString = new byte[maxSize];
        for (int i = 0; i < maxSize; ++i) {
            toString[i] = memory[address + i];
            if (memory[address + i] == '\0') {
                break;
            }
        }
        return Conversion.bytesToString(toString);
    }

    /**
     * Writes data to the specified address.
     *
     * @param address the address to write at.
     * @param data    the data to write.
     */
    public void write(int address, byte[] data) {
        address = address - OFFSET;
        if (address < 0 || (address + data.length) > this.MEMORY_SIZE) {
            // Error: address is out of bounds
            // TODO replace with new simulation exception
            System.err.println("Error: address is out of bounds");
            return;
        }
        System.arraycopy(data, 0, memory, address, data.length);
    }

    /**
     * Writes a long to the specified address.
     *
     * @param address the address to write at.
     * @param data    the long to write.
     */
    public void writeLong(int address, long data) {
        write(address, Conversion.longToBytes(data));
    }

    /**
     * Writes a String to the specified address.
     *
     * @param address the address to write at.
     * @param data    the String to write.
     * @param maxSize the maximum size of the String to be in bytes.
     */
    public void writeString(int address, String data, int maxSize) {
        address = address - OFFSET;
        if (maxSize < 0) {
            System.out.println("Error: max string size cannot be less than zero");
            return;
        }
        if (address < 0 || (address + data.getBytes().length) >= this.MEMORY_SIZE) {
            // Error: address is out of bounds
            System.out.println("Error: address is out of bounds");
            return;
        }

        for (int i = 0; i < data.getBytes().length && i < maxSize; ++i) {
            memory[address + i] = data.getBytes()[i];
        }

        if (maxSize <= data.getBytes().length) {
            memory[address + maxSize] = '\0';
        }
    }

}
