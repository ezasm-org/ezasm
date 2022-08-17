package EzASM;

import EzASM.parsing.ParseException;

public class Main {

    // Temporary tests
    public static void main(String[] args) throws ParseException {
        Simulator sim = new Simulator();
        sim.readLine("add $s0 $s1 100");
        System.out.println(sim.getRegister("s0"));
        sim.readLine("add $s1 $s0 $s0");
        System.out.println(sim.getRegister("s1"));

        Memory mem = new Memory();
        int location = mem.allocate(8);
        mem.writeLong(location, 123456789012345L);
        System.out.println(mem.readLong(location));
        int location2 = mem.allocate(1024);
        mem.writeString(location2, "Hello memory!", 1024);
        System.out.println(mem.readString(location2, 1024));
    }

}