package EzASM;

import EzASM.instructions.InstructionDispatcher;
import EzASM.simulation.Memory;
import EzASM.simulation.Simulator;

import java.io.File;

import static EzASM.Arguments.handleArgs;

/**
 * The main entrypoint of the program.
 */
public class Main {

    /**
     * The main function entrypoint of the program
     * @param args the program arguments.
     */
    public static void main(String[] args) {
        // Temporary tests
        //test();
        //testFile();
        handleArgs(args);
    }

    /**
     * A simple test cases for the program.
     */
    private static void test() {
        try {
            System.out.println(InstructionDispatcher.getInstructions().keySet());
            Simulator sim = new Simulator();
            sim.executeLine("add $s0 $0 -69");
            sim.executeLine("add $s1 $0 420");
            sim.executeLine("mul $s3 $s0 $s1");
            sim.executeLine("div $s3 $s3 $s1");
            sim.executeLine("sll $s4 $s3 4");
            System.out.println(sim.getRegister("s0").toDecimalString());
            System.out.println(sim.getRegister("s1").toDecimalString());
            System.out.println(sim.getRegister("s3").toDecimalString());

            System.out.println(sim.getRegisters().toString());

            Memory mem = new Memory();
            int location = mem.allocate(8);
            mem.writeLong(location, 123456789012345L);
            System.out.println(mem.readLong(location));
            int location2 = mem.allocate(1024);
            mem.writeString(location2, "Hello memory!", 1024);
            System.out.println(mem.readString(location2, 1024));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests reading a program from a given file.
     */
    private static void testFile() {
        try {
            Simulator sim = new Simulator();
            String content = FileIO.readFile(new File("res/example.ez"));
            sim.readMultiLineString(content);
            sim.runLinesFromPC();
            System.out.println(sim.getRegisters().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}