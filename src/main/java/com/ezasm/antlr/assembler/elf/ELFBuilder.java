package com.ezasm.antlr.assembler.elf;

import com.google.common.primitives.Bytes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ezasm.antlr.assembler.elf.ELF.*;

public class ELFBuilder {
    private Header header;
    private ArrayList<Section> sections;
    private ArrayList<String> sectionNames;

    public ELFBuilder(ArrayList<List<Byte>> sections, Map<String, Integer> sectionNames) {
        this.header = new ELF.Header();
        this.sections = new ArrayList<>();
        this.sectionNames = new ArrayList<>();

        String[] sectionNamesTemp = new String[sectionNames.size()];

        sectionNames.forEach((s, i) -> sectionNamesTemp[i] = s);

        int addr = 0x10000;

        Section nullSection = new Section();
        nullSection.data = new byte[0];
        nullSection.header = new Section.Header();
        nullSection.header.addr = 0;
        nullSection.header.entrySize = 0;
        nullSection.header.type = 0;
        nullSection.header.offset = 0;
        nullSection.header.addrAlign = 0;
        nullSection.header.flags = 0;
        nullSection.header.size = 0;
        nullSection.header.info = 0;
        nullSection.header.link = 0;

        addSection(nullSection, "NULL");

        for (int i = 0; i < sections.size(); i++) {
            Section section = new Section();
            section.data = Bytes.toArray(sections.get(i));
            section.header = new Section.Header();
            section.header.type = 0x1; // Program data
            section.header.addrAlign = 1;
            section.header.flags = 0x4 | 0x2 | 0x1; // executable, allocated, and writeable
            section.header.addr = addr;
            addSection(section, sectionNamesTemp[i]);

            addr += section.data.length;
        }
    }

    public void write(FileOutputStream os) throws IOException {
        createStringTable();
        Program program = createProgramHeader();

        header.phNum = 1;
        header.shNum = (byte)sections.size();

        header.shStrI = (short) (sections.size() - 1);

        header.entry = 0x10000 + header.hSize + header.phEntrySize + sections.size() * header.shEntrySize;

        int filePos = 0;
        int dataSize = 0;
        for (Section section : sections) {
            dataSize += section.data.length;
        }

        program.header.offset = header.hSize + header.phEntrySize + sections.size() * header.shEntrySize;
        program.header.virtAddr = 0x10000 + header.hSize + header.phEntrySize + sections.size() * header.shEntrySize;
        program.header.physAddr = 0x10000 + header.hSize + header.phEntrySize + sections.size() * header.shEntrySize;
        program.header.fileSize = dataSize - sections.get(header.shStrI).data.length;
        program.header.memSize = dataSize - sections.get(header.shStrI).data.length;

        byte[] data = new byte[dataSize];

        for (int i = 0; i < sections.size(); i++) {
            System.arraycopy(sections.get(i).data, 0, data, filePos, sections.get(i).data.length);
            sections.get(i).header.offset = header.hSize + header.phEntrySize + sections.size() * header.shEntrySize + filePos;
            sections.get(i).header.addr += sections.get(i).header.offset;
            sections.get(i).header.size = sections.get(i).data.length;
            filePos += sections.get(i).data.length;
        }

        byte[] headers = new byte[header.hSize + header.phEntrySize + sections.size() * header.shEntrySize];
        System.arraycopy(header.getBytes(), 0, headers, 0, header.hSize);
        System.arraycopy(program.header.getBytes(), 0, headers, header.hSize, header.phEntrySize);
        for (int i = 0; i < sections.size(); i++) {
            System.arraycopy(sections.get(i).header.getBytes(), 0, headers, header.hSize + header.phEntrySize + i * header.shEntrySize, header.shEntrySize);
        }


        os.write(headers);
        os.write(data);
    }

    public void addSection(Section section, String name) {
        sections.add(section);
        sectionNames.add(name);
    }

    private void createStringTable() {
        Section entry = new Section();
        entry.header = new Section.Header();
        entry.header.addr = 0;
        entry.header.addrAlign = 1;
        entry.header.info = 0;
        entry.header.link = 0;
        entry.header.flags = 0;
        entry.header.entrySize = 0;
        entry.header.name = 1;
        entry.header.offset = 0x0;
        entry.header.type = 0x3;

        byte[] idBytes = ".shstrtab".getBytes();

        ArrayList<Byte> data = new ArrayList<>();
        data.add((byte)0);

        for (int i = 0; i < idBytes.length; i++) {
            data.add(idBytes[i]);
        }
        data.add((byte)0);

        for (String name : sectionNames) {
            byte[] bytes = name.getBytes();
            for (byte b : bytes) {
                data.add(b);
            }
            data.add((byte)0);
        }

        int ptr = idBytes.length + 2;
        for (int i = 0; i < sections.size(); i++) {
            sections.get(i).header.name = ptr;
            ptr += sectionNames.get(i).length() + 1;
        }

        entry.header.size = data.size();
        entry.data = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            entry.data[i] = data.get(i);
        }

        sections.add(entry);
    }

    private Program createProgramHeader() {
        Program program = new Program();
        program.header = new Program.Header();
        program.header.type = 1;
        program.header.physAddr = 0x10000;
        program.header.virtAddr = 0x10000;
        program.header.align = 0x10000;
        program.header.flags = 7; //Read Write Exec

        return program;
    }
}
