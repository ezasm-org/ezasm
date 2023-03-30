package com.ezasm.simulation;

import com.ezasm.simulation.exception.*;
import com.ezasm.util.RawData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the system memory. There will be a single and contiguous array of memory which represents both the stack
 * and heap with the stack growing downward and the heap growing upward. Implements an "offset" for the address spacing
 * to not start at 0. Keeps track of allocated memory and throws an exception when the heap crosses the stack. Has a
 * default size of 2^21 words (or 2^24 bytes). Has a default word size of 8 bytes (the typical long integer or long
 * float size).
 */
public class Memory {

    /**
     * The default number of words possible to store in the system.
     */
    public static final int DEFAULT_MEMORY_WORDS = 0x20_0000;

    /**
     * The default word size of the system.
     */
    public static final int DEFAULT_WORD_SIZE = 4;

    private static int wordSize = DEFAULT_WORD_SIZE;

    public static int wordSize() {
        return wordSize;
    }

    private final int DEFAULT_OFFSET = 0x1_0000;
    private final int STRING_OFFSET = 0x1_0000;

    private final int memorySize;
    private final int offsetBytes;
    private final int disallowedBytes;
    private final byte[] memory;
    private int alloc;
    private int stringAlloc;

    private final Map<String, RawData> stringAddressMap;

    /**
     * Constructs memory with the default parameters.
     */
    public Memory() {
        this.offsetBytes = wordSize * (DEFAULT_OFFSET + STRING_OFFSET);
        this.disallowedBytes = wordSize * DEFAULT_OFFSET;
        this.memorySize = offsetBytes + DEFAULT_MEMORY_WORDS * wordSize;
        this.memory = new byte[memorySize];
        this.alloc = offsetBytes;
        this.stringAlloc = STRING_OFFSET * wordSize;
        this.stringAddressMap = new HashMap<>();
    }

    /**
     * Constructs memory with different parameters for word size and memory size.
     *
     * @param wordSize   the word size in bytes.
     * @param memorySize the memory size in words.
     */
    public Memory(int wordSize, int memorySize) {
        Memory.wordSize = wordSize;
        this.offsetBytes = wordSize * (DEFAULT_OFFSET + STRING_OFFSET);
        this.disallowedBytes = wordSize * DEFAULT_OFFSET;
        this.memorySize = offsetBytes + memorySize * wordSize;
        this.memory = new byte[this.memorySize];
        this.alloc = offsetBytes;
        this.stringAlloc = STRING_OFFSET * wordSize;
        this.stringAddressMap = new HashMap<>();
    }

    /**
     * Resets the memory by setting all values to zero and returning the allocation pointer to zero.
     */
    public void reset() {
        Arrays.fill(memory, (byte) 0);
        alloc = offsetBytes;
        stringAlloc = STRING_OFFSET * wordSize;
        stringAddressMap.clear();
    }

    /**
     * Gets the size of the memory as a number of words.
     *
     * @return the size of the memory as a number of words.
     */
    public int size() {
        return memorySize;
    }

    /**
     * Gets the initial stack pointer of the memory.
     *
     * @return the initial stack pointer of the memory.
     */
    public int initialStackPointer() {
        return memorySize;
    }

    /**
     * Gets the initial heap pointer of the memory.
     *
     * @return the initial heap pointer of the memory.
     */
    public int initialHeapPointer() {
        return offsetBytes;
    }

    /**
     * Gets the current heap pointer of the memory.
     *
     * @return the current heap pointer of the memory.
     */
    public int currentHeapPointer() {
        return alloc;
    }

    /**
     * Sets the current heap pointer of the memory.
     *
     * @param address the new heap pointer for the memory.
     */
    public void setHeapPointer(int address) throws SimulationException {
        if (address < offsetBytes || address > this.memorySize) {
            throw new AllocationException(address, address - alloc);
        }
        alloc = address;
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
        if (alloc + bytes > sp) {
            throw new SimulationException(String.format(
                    "Allocating %d bytes with $SP at %d would cause the heap to overwrite the stack", bytes, sp));
        }
        int address = alloc;
        alloc = alloc + bytes;
        return address;
    }

    /**
     * Allocates a certain number of bytes without a check based on stack pointer.
     *
     * @param bytes the number of bytes to allocate.
     * @return the allocated memory starting point.
     */
    public int unsafeAllocate(int bytes) {
        int address = alloc;
        alloc = alloc + bytes;
        return address;
    }

    /**
     * Gets the information from the memory at a certain address.
     *
     * @param address the address to begin to read from.
     * @param count   the number of bytes to read.
     * @return the information read from the memory at a certain address.
     */
    public RawData read(int address, int count) throws ReadOutOfBoundsException {
        if (address < disallowedBytes || address + count > this.memorySize) {
            throw new ReadOutOfBoundsException(address);
        }
        return new RawData(Arrays.copyOfRange(memory, address, address + count));
    }

    /**
     * Gets one word of information from the memory at a certain address.
     *
     * @param address the address to begin to read from.
     * @return the information read from the memory at a certain address.
     */
    public RawData read(int address) throws ReadOutOfBoundsException {
        return read(address, wordSize);
    }

    /**
     * Writes data to the specified address.
     *
     * @param address the address to write at.
     * @param data    the data to write.
     */
    public void write(int address, RawData data) throws SimulationException {
        if (address < 0 || address + data.data().length > this.memorySize) {
            throw new WriteOutOfBoundsException(address);
        } else if (address < offsetBytes) {
            throw new WriteToReadOnlyException(address);
        }
        System.arraycopy(data.data(), 0, memory, address, data.data().length);
    }

    /**
     * Writes data to the specified address.
     *
     * @param address the address to write at.
     * @param data    the data to write.
     */
    public void unsafeWrite(int address, RawData data) throws WriteOutOfBoundsException {
        if (address < 0 || address + data.data().length > this.memorySize) {
            throw new WriteOutOfBoundsException(address);
        }
        System.arraycopy(data.data(), 0, memory, address, data.data().length);
    }

    /**
     * Writes string immediates to read-only memory if it is not already in there.
     *
     * @param strings the list of strings to write.
     * @throws SimulationException if there is an error writing or the writing goes out of the reserved area.
     */
    public void addStringImmediates(List<String> strings) throws SimulationException {
        for (String string : strings) {
            if (!stringAddressMap.containsKey(string)) {
                // Write the string into read-only string memory
                if (stringAlloc + string.length() >= offsetBytes) {
                    throw new SimulationException("Attempted to write more string immediate bytes then possible");
                }
                for (int i = 0; i < string.length(); ++i) {
                    unsafeWrite(stringAlloc + i * wordSize, new RawData(string.charAt(i)));
                }
                unsafeWrite(stringAlloc + string.length() * wordSize, RawData.emptyBytes(wordSize));

                stringAddressMap.put(string, new RawData(stringAlloc));
                stringAlloc += (string.length() + 1) * wordSize;
            }
        }
    }

    /**
     * Gets the address of a string immediate.
     *
     * @param string the string immediate to get the address of.
     * @return the address of the string immediate wrapped as a RawData.
     * @throws SimulationException if the given immediate does not exist.
     */
    public RawData getStringImmediateAddress(String string) throws SimulationException {
        if (!stringAddressMap.containsKey(string)) {
            throw new SimulationException(String.format("String '%s' not found in memory", string));
        }
        return stringAddressMap.get(string);
    }

}
