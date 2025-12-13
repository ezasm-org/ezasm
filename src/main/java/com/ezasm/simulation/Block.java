package com.ezasm.simulation;

/**
 * A simple class for information about a chunk of memory (namely location and size)
 */
public class Block {
    public long addr;
    public long size;

    public Block(long addr, long size) {
        this.addr = addr;
        this.size = size;
    }
}
