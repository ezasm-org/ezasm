package EzASM;

import EzASM.gui.Window;
import EzASM.instructions.InstructionDispatcher;
import EzASM.simulation.Memory;
import EzASM.simulation.Simulator;
import org.apache.commons.cli.*;

import javax.swing.*;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        // Temporary tests
        //test();
        //testFile();
        handleArgs(args);
    }

    public static void handleArgs(String[] args) {
        Options options = new Options();

        Option windowlessOption = new Option("w", "windowless", false,
                "Starts the program in windowless mode \n(default: disabled)");
        options.addOption(windowlessOption);

        Option fileOption = new Option("f", "file", true,
                "EzASM code file path to open");
        fileOption.setArgName("path");
        options.addOption(fileOption);

        Option memoryOption = new Option("m", "memory", true,
                "The number of words to allocate space for on the stack and heap each, " +
                        "must be larger than 0 (default 65536)");
        options.addOption(memoryOption);
        memoryOption.setArgName("words");

        Option wordSizeOption = new Option("s", "word-size", true,
                "The size in bytes of a word (default: 8)");
        options.addOption(wordSizeOption);
        wordSizeOption.setArgName("word size");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;

        try {
            commandLine = parser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException e) {
            errorArgs(e.getMessage());
        }

        int memorySize = 0;
        int wordSize = 0;

        if(commandLine.hasOption(memoryOption)) {
            String memoryString = commandLine.getOptionValue(memoryOption);
            try {
                memorySize = Integer.parseInt(memoryString);
            } catch (Exception e) {
                errorArgs("Unable to parse given word size");
            }
        } else {
            memorySize = Memory.DEFAULT_MEMORY_WORDS;
        }
        if(commandLine.hasOption(wordSizeOption)) {
            String wordSizeString = commandLine.getOptionValue(wordSizeOption);
            try {
                wordSize = Integer.parseInt(wordSizeString);
            } catch (Exception e) {
                errorArgs("Unable to parse given word size");
            }
        } else {
            wordSize = Memory.DEFAULT_WORD_SIZE;
        }

        Simulator sim = new Simulator(wordSize, memorySize);
        String filepath = "";
        if(commandLine.hasOption(fileOption)) {
            filepath = commandLine.getOptionValue(fileOption);
        }

        if(commandLine.hasOption(windowlessOption)) {
            CommandLineInterface cli = null;
            if(filepath.equals("")) {
                cli = new CommandLineInterface(sim);
            } else {
                cli = new CommandLineInterface(sim, filepath);
            }
            cli.startSimulation();
        } else {
            SwingUtilities.invokeLater(() -> {
                Window.getInstance(sim);
            });
        }
    }

    private static void errorArgs(String message) {
        System.err.println(message);
        System.exit(1);
    }

    public static void test() {
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

    public static void testFile() {
        try {
            Simulator sim = new Simulator();
            System.out.println(System.getProperty("user.dir"));
            String content = FileIO.readFile(new File("res/example.ez"));
            sim.readMultiLineString(content);
            sim.runLinesFromPC();
            System.out.println(sim.getRegisters().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}