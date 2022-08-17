package EzASM;

import EzASM.instructions.InstructionDispatcher;
import EzASM.parsing.ParseException;

public class Main {

    // Temporary tests
    public static void main(String[] args) throws ParseException {
        System.out.println(InstructionDispatcher.getInstructions().keySet());
        Simulator sim = new Simulator();
        sim.readLine("add $s0 $0 -69");
        sim.readLine("add $s1 $0 420");
        sim.readLine("mul $s3 $s0 $s1");
        sim.readLine("div $s3 $s3 $s1");
        sim.readLine("slr $s4 $s3 4");
        System.out.println(sim.getRegister("s0").toDecimalString());
        System.out.println(sim.getRegister("s1").toDecimalString());
        System.out.println(sim.getRegister("s3").toDecimalString());

        System.out.println(sim.registryToString());

        Memory mem = new Memory();
        int location = mem.allocate(8);
        mem.writeLong(location, 123456789012345L);
        System.out.println(mem.readLong(location));
        int location2 = mem.allocate(1024);
        mem.writeString(location2, "Hello memory!", 1024);
        System.out.println(mem.readString(location2, 1024));
    }

}