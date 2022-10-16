package com.ezasm.assembler.elf;

public class ELF {
    private static byte[] toBytes(short value) {
        byte[] bytes = new byte[2];

        bytes[0] = (byte)(value >> 8);
        bytes[1] = (byte)(value);

        return bytes;
    }

    private static byte[] toBytes(int value) {
        byte[] bytes = new byte[4];

        bytes[0] = (byte)(value >> 24);
        bytes[1] = (byte)(value >> 16);
        bytes[2] = (byte)(value >> 8);
        bytes[3] = (byte)(value);

        return bytes;
    }

    private static byte[] toBytes(long value) {
        byte[] bytes = new byte[8];

        bytes[0] = (byte)(value >> 56);
        bytes[1] = (byte)(value >> 48);
        bytes[2] = (byte)(value >> 40);
        bytes[3] = (byte)(value >> 32);
        bytes[4] = (byte)(value >> 24);
        bytes[5] = (byte)(value >> 16);
        bytes[6] = (byte)(value >> 8);
        bytes[7] = (byte)(value);

        return bytes;
    }

    private static byte[] concat(byte[]... a) {
        int length = 0;
        for (byte[] aa : a) {
            length += aa.length;
        }

        byte[] bytes = new byte[length];
        int i = 0;
        for (byte[] aa : a) {
            System.arraycopy(aa, 0, bytes, i, aa.length);
            i += aa.length;
        }

        return bytes;
    }

    public static class Header {
        public byte[] idSignature = {0x7F, 0x45, 0x4c, 0x46};
        public byte idClass = 1;
        public byte idData = 2;
        public byte idVersion = 1;
        public byte idOSabi = 0x03;
        public long idPad = 0;
        public short type = 0x01;
        public short machine = 0x08;
        public int version = 1;
        public int entry = 0;
        public int phOff = 0;
        public int shOff = 52;
        public int flags = 0b110;
        public short hSize = 52;
        public short phEntrySize = 0x20;
        public short phNum = 0;
        public short shEntrySize = 0x28;
        public short shNum = 0;
        public short shStrI = 1;

        public byte[] getBytes() {
            return concat(idSignature,
                    new byte[] {idClass, idData, idVersion, idOSabi},
                    toBytes(idPad),
                    toBytes(type),
                    toBytes(machine),
                    toBytes(version),
                    toBytes(entry),
                    toBytes(phOff),
                    toBytes(shOff),
                    toBytes(flags),
                    toBytes(hSize),
                    toBytes(phEntrySize),
                    toBytes(phNum),
                    toBytes(shEntrySize),
                    toBytes(shNum),
                    toBytes(shStrI)
            );
        }
    }

    public static class Program {
        public Header header;
        public byte[] data;

        public static class Header {
            public int type;
            public int offset;
            public int virtAddr;
            public int physAddr;
            public int fileSize;
            public int memSize;
            public int flags;
            public int align;

            public byte[] getBytes() {
                return concat(
                        toBytes(type),
                        toBytes(offset),
                        toBytes(virtAddr),
                        toBytes(physAddr),
                        toBytes(fileSize),
                        toBytes(memSize),
                        toBytes(flags),
                        toBytes(align)
                );
            }
        }
    }

    public static class Section {
        public Header header;
        public byte[] data;

        public static class Header {
            public int name;
            public int type;
            public int flags;
            public int addr;
            public int offset;
            public int size;
            public int link;
            public int info;
            public int addrAlign;
            public int entrySize;

            public byte[] getBytes() {
                return concat(
                        toBytes(name),
                        toBytes(type),
                        toBytes(flags),
                        toBytes(addr),
                        toBytes(offset),
                        toBytes(size),
                        toBytes(link),
                        toBytes(info),
                        toBytes(addrAlign),
                        toBytes(entrySize)
                );
            }
        }
    }
}
