package com.ezasm.util;

import com.ezasm.gui.settings.Config;
import com.ezasm.gui.Window;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.Memory;
import org.apache.commons.cli.*;

/**
 * Methods to handle the program arguments and begin the program correspondingly.
 */
public class Arguments {

    /**
     * Handles the program arguments and begin the program correspondingly.
     *
     * @param args the program arguments.
     */
    public static void handleArgs(String[] args) {
        Config config = new Config();
        Options options = new Options();

        Option helpOption = new Option("h", "help", false, "Display this information");
        options.addOption(helpOption);

        Option verionOption = new Option("v", "version", false, "Display the program version");
        options.addOption(verionOption);

        Option windowlessOption = new Option("w", "windowless", false,
                "Starts the program in windowless mode\n(default: disabled)");
        options.addOption(windowlessOption);

        Option memoryOption = new Option("m", "memory", true,
                "The number of words to allocate space for on the stack and heap each; must be larger than 0\n(default: 0x20_0000)");
        options.addOption(memoryOption);
        memoryOption.setArgName("memory size");

        Option wordSizeOption = new Option("s", "word-size", true, "The size in bytes of a word\n(4 or 8, default: 4)");
        options.addOption(wordSizeOption);
        wordSizeOption.setArgName("word size");

        Option debugOption = new Option("d", "debug", false,
                "Run in debug mode. Output errors to the terminal instead of the integrated console.");
        options.addOption(debugOption);
        debugOption.setArgName("debug mode");

        Option inputOption = new Option("i", "input", true, "A file to receive standard input from (default: none)");
        options.addOption(inputOption);
        inputOption.setArgName("input file path");

        Option outputOption = new Option("o", "output", true, "A file to send standard output to (default: none)");
        options.addOption(outputOption);
        outputOption.setArgName("output file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;

        try {
            commandLine = parser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException e) {
            errorArgs(options, e.getMessage());
        }

        if (commandLine.hasOption(helpOption)) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("EzASM [options] [code file]", options);
            System.exit(0);
        }

        if (commandLine.hasOption(verionOption)) {
            SystemStreams.out.printf(String.format("%s %s\n", MavenProperties.NAME, MavenProperties.VERSION));
            System.exit(0);
        }

        int memorySize = 0;
        int wordSize = 0;

        if (commandLine.hasOption(wordSizeOption)) {
            String wordSizeString = commandLine.getOptionValue(wordSizeOption);
            try {
                wordSize = Integer.parseInt(wordSizeString);
                if (wordSize != 4 && wordSize != 8) {
                    errorArgs(options, "Word size must be 4 or 8");
                }
            } catch (Exception e) {
                errorArgs(options, "Unable to parse given word size");
            }
        } else {
            wordSize = Memory.DEFAULT_WORD_SIZE;
        }

        if (commandLine.hasOption(memoryOption)) {
            String memoryString = commandLine.getOptionValue(memoryOption);
            try {
                memorySize = Integer.parseInt(memoryString);
                if (memorySize < 0) {
                    errorArgs(options, "Memory size must be positive");
                } else if (memorySize < wordSize) {
                    errorArgs(options, "Memory must be at least 1 word");
                }
            } catch (Exception e) {
                errorArgs(options, "Unable to parse given memory size");
            }
        } else {
            memorySize = Memory.DEFAULT_MEMORY_WORDS;
        }

        Simulator sim = new Simulator(wordSize, memorySize);
        String filepath = "";

        if (commandLine.getArgs().length > 1) {
            errorArgs(options, "Program can only accept one code file");
        } else if (commandLine.getArgs().length > 0) {
            filepath = commandLine.getArgs()[0];
        }

        String inputpath = "";
        if (commandLine.hasOption(inputOption)) {
            inputpath = commandLine.getOptionValue(inputOption);
        }

        String outputpath = "";
        if (commandLine.hasOption(outputOption)) {
            outputpath = commandLine.getOptionValue(outputOption);
        }

        boolean debugMode = false;
        if (commandLine.hasOption(debugOption)) {
            debugMode = true;
        }

        if (commandLine.hasOption(windowlessOption)) {
            CommandLineInterface cli;
            if (filepath.equals("")) {
                cli = new CommandLineInterface(sim);
            } else if (inputpath.equals("") && outputpath.equals("")) {
                cli = new CommandLineInterface(sim, filepath);
            } else {
                cli = new CommandLineInterface(sim, filepath, inputpath, outputpath);
            }
            cli.startSimulation();
        } else {
            if (!inputpath.equals("") || !outputpath.equals("")) {
                Window.instantiate(sim, config, debugMode, inputpath, outputpath);
            } else {
                Window.instantiate(sim, config, debugMode);
            }
        }
    }

    /**
     * Exit the program while displaying a message.
     *
     * @param message the message to print before exiting.
     */
    private static void errorArgs(Options options, String message) {
        SystemStreams.err.println(message);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("EzASM [options] [code file]", options);
        System.exit(1);
    }

}
