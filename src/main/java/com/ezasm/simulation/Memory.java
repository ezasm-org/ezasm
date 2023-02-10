package com.ezasm.simulation;

import com.ezasm.Conversion;
import com.ezasm.simulation.exception.SimulationAddressOutOfBoundsException;
import com.ezasm.simulation.exception.SimulationException;

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
    public int allocate(int bytes, int sp) throws SimulationException {
        if (alloc + bytes + OFFSET > sp) {
            throw new SimulationException(String.format(
                    "Allocating %d bytes with $SP at %d would cause the heap to overwrite the stack", bytes, sp));
        }
        int addr = alloc;
        alloc = alloc + bytes;
        return addr + OFFSET;
    }

    /**
     * Allocates a certain number of bytes without a check based on stack pointer.
     *
     * @param bytes the number of bytes to allocate.
     * @return the allocated memory starting point.
     */
    public int unsafeAllocate(int bytes) {
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
    public byte[] read(int address, int count) throws SimulationAddressOutOfBoundsException {
        address = address - OFFSET;
        if (address < 0 || (address + count) > this.MEMORY_SIZE) {
            throw new SimulationAddressOutOfBoundsException(address + OFFSET);
        }
        return Arrays.copyOfRange(memory, address, address + count);
    }

    /**
     * Gets one word of information from the memory at a certain address.
     *
     * @param address the address to begin to read from.
     * @return the information read from the memory at a certain address.
     */
    public byte[] read(int address) throws SimulationAddressOutOfBoundsException {
        return read(address, WORD_SIZE);
    }

    /**
     * Gets the information from the memory at a certain address interpreted as a long.
     *
     * @param address the address to begin to read from.
     * @return the long read from the memory at a certain address.
     */
    public long readLong(int address) throws SimulationAddressOutOfBoundsException {
        return Conversion.bytesToLong(read(address, WORD_SIZE));
    }

    /**
     * Gets the information from the memory at a certain address interpreted as a String.
     *
     * @param address the starting address to read from.
     * @param maxSize the maximum size of the String to be in bytes.
     * @return the String interpreted.
     */
    public String readString(int address, int maxSize) throws SimulationException {
        if (maxSize < 0) {
            throw new SimulationException(String.format("String size cannot be %d", maxSize));
        }
        if (address < 0 || (address - OFFSET + maxSize) >= this.MEMORY_SIZE) {
            throw new SimulationAddressOutOfBoundsException(address + OFFSET);
        }

        String toString = "";
        for (int i = 0; i < maxSize; ++i) {
            char read = (char) Conversion.bytesToLong(read(address + (i * WORD_SIZE)));
            toString += read;
            if (read == '\0') {
                System.out.println(i);
                break;
            }
        }
        return toString;
    }

    /**
     * Writes data to the specified address.
     *
     * @param address the address to write at.
     * @param data    the data to write.
     */
    public void write(int address, byte[] data) throws SimulationAddressOutOfBoundsException {
        address = address - OFFSET;
        if (address < 0 || (address + data.length) > this.MEMORY_SIZE) {
            throw new SimulationAddressOutOfBoundsException(address + OFFSET);
        }
        System.arraycopy(data, 0, memory, address, data.length);
    }

    /**
     * Writes a long to the specified address.
     *
     * @param address the address to write at.
     * @param data    the long to write.
     */
    public void writeLong(int address, long data) throws SimulationAddressOutOfBoundsException {
        write(address, Conversion.longToBytes(data));
    }

    /**
     * Writes a String to the specified address.
     *
     * @param address the address to write at.
     * @param data    the String to write.
     * @param maxSize the maximum size of the String to be in bytes.
     */
    public void writeString(int address, String data, int maxSize) throws SimulationException {
        address = address - OFFSET;
        if (maxSize < 0) {
            throw new SimulationException(String.format("String size cannot be %d", maxSize));
        }
        if (address < 0 || (address + data.getBytes().length) >= this.MEMORY_SIZE) {
            throw new SimulationAddressOutOfBoundsException(address + OFFSET);
        }

        for (int i = 0; i < data.getBytes().length && i < maxSize; ++i) {
            memory[address + i] = data.getBytes()[i];
        }

        if (maxSize <= data.getBytes().length) {
            memory[address + maxSize] = '\0';
        }
    }

}
