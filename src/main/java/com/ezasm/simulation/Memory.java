package com.ezasm.simulation;

import com.ezasm.gui.settings.Config;
import com.ezasm.simulation.exception.*;
import com.ezasm.util.RawData;

import java.util.*;

/**
 * Represents the system memory. There will be a single and contiguous array of memory which represents both the stack
 * and heap with the stack growing downward and the heap growing upward. Implements an "offset" for the address spacing
 * to not start at 0. Keeps track of allocated memory and throws an exception when the heap crosses the stack. Has a
 * default size of 2^21 words (or 2^24 bytes). Has a default word size of 8 bytes (the typical long integer or long
 * float size).
 */
public class Memory {
    private Config config;
    /**
     * The default number of words possible to store in the system.
     */
    public static final int DEFAULT_MEMORY_WORDS = 0x20_0000;

    /**
     * The default word size of the system.
     */
    public static final int DEFAULT_WORD_SIZE = 4;

    private static int wordSize = DEFAULT_WORD_SIZE;

    private final int DEFAULT_OFFSET = 0x1_0000;
    private final int STRING_OFFSET = 0x1_0000;

    private final int memorySize;
    private final int offsetBytes;
    private final int disallowedBytes;
    private final byte[] memory;
    private int alloc;
    private int stringAlloc;

    private final Map<String, RawData> stringAddressMap;

    private final Map<Long, Long> allocationsMap; // map the starting address of a alloc'd block to it's size
    private final TreeSet<Block> freeList;

    /**
     * Constructs memory with the default parameters.
     */
    public Memory() {
        this.config = new Config();
        this.offsetBytes = wordSize * (DEFAULT_OFFSET + STRING_OFFSET);
        this.disallowedBytes = wordSize * DEFAULT_OFFSET;
        this.memorySize = offsetBytes + DEFAULT_MEMORY_WORDS * wordSize;
        this.memory = new byte[memorySize];
        this.alloc = offsetBytes;
        this.stringAlloc = STRING_OFFSET * wordSize;
        this.stringAddressMap = new HashMap<>();
        this.allocationsMap = new HashMap<>();
        this.freeList = new TreeSet<>(Comparator.comparingLong(b -> b.addr));
        resetMemory();
    }

    /**
     * Constructs memory with different parameters for word size and memory size.
     *
     * @param wordSize   the word size in bytes.
     * @param memorySize the memory size in words.
     */
    public Memory(int wordSize, int memorySize) {
        this.config = new Config();
        Memory.wordSize = wordSize;
        this.offsetBytes = wordSize * (DEFAULT_OFFSET + STRING_OFFSET);
        this.disallowedBytes = wordSize * DEFAULT_OFFSET;
        this.memorySize = offsetBytes + memorySize * wordSize;
        this.memory = new byte[this.memorySize];
        this.alloc = offsetBytes;
        this.stringAlloc = STRING_OFFSET * wordSize;
        this.stringAddressMap = new HashMap<>();
        this.allocationsMap = new HashMap<>();
        this.freeList = new TreeSet<>(Comparator.comparingLong(b -> b.addr));
        resetMemory();
    }

    /**
     * Gets the word size to use program-wide.
     *
     * @return the word size to use program-wide.
     */
    public static int getWordSize() {
        return wordSize;
    }

    /**
     * Resets the memory by setting all values to zero and returning the allocation pointer to zero.
     */
    public void reset() {
        alloc = offsetBytes;
        stringAlloc = STRING_OFFSET * wordSize;
        stringAddressMap.clear();
        resetMemory();
        this.allocationsMap.clear();
        this.freeList.clear();
    }

    /**
     * Reset the memory to either be randomized or be all zeroes, depending on the config setting.
     */
    private void resetMemory() {
        // resetting randomizes each byte of memory
        if (config.getMemoryRandomizeOnReset()) {
            byte[] bytes = new byte[1];
            Random random = new Random();
            random.setSeed(System.nanoTime());
            for (int i = offsetBytes; i < memory.length; ++i) {
                random.nextBytes(bytes);
                memory[i] = bytes[0];
            }
        }
        // resetting sets each byte of memory to 0
        else {
            Arrays.fill(memory, (byte) 0);
        }
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
     * Gets the initial text pointer of the memory.
     *
     * @return the initial text pointer of the memory.
     */
    public int initialTextPointer() {
        return STRING_OFFSET * wordSize;
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
     * Gets the currently allocated heap memory
     *
     * @return the currently allocations map
     */
    public Map<Long, Long> getAllocations() { return allocationsMap; }

    /**
     * Gets the list of free blocks of memory up to heap pointer
     *
     * @return the list of blocks
     */
    public TreeSet<Block> getFreeList() { return freeList; }

    /**
     * Gets (the first) free address in the free list that is at least min bytes
     *  and updates the free list
     *
     * @param need the number of bytes needed
     * @return Block of size
     */
    public Block getFreeBlock(long need) {
        for (Iterator<Block> it = freeList.iterator(); it.hasNext();) {
            Block block = it.next();
            if (block.size >= need) {
                long addr = block.addr; // found a block!
                if (block.size == need) {
                    // no left-over space (free list is decremented)
                    it.remove();
                } else {
                    // (block.size - min) bytes starting at (block.addr + min) left after allocation
                    block.size -= need;
                    block.addr += need;
                }
                return new Block(addr, need);
            }
        }
        // check last element, if it's up against the hp, return it
        if (!freeList.isEmpty()) {
            Block last = freeList.last();
            if (last.addr + last.size == alloc) {
                freeList.remove(last);
                return last;
            }
        }

        return null;
    }

    /**
     * Insert a new block into the free list
     *  also check neighbors and merges if possible
     *
     * @param addr the base address of an alloc'd block
     * @param size the size of the alloc'd block
     */
    public void addToFreeList(long addr, long size) {
        Block curr = new Block(addr, size);
        Block before = freeList.lower(curr);
        Block after = freeList.higher(curr);

        // check adj to prior block
        if (before != null && before.addr + before.size == curr.addr) {
            curr.addr = before.addr; // merge to prior
            curr.size += before.size;
            freeList.remove(before);
        }

        // check adj to subsequent block
        if (after != null && curr.addr + curr.size == after.addr) {
            curr.size += after.size; // merge to subsequent
            freeList.remove(after);
        }

        freeList.add(curr);
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
