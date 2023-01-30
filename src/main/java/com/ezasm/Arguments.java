package com.ezasm;

import com.ezasm.gui.Window;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.Simulator;
import org.apache.commons.cli.*;

import javax.swing.*;

/**
 * Methods to handle the program arguments and begin the program correspondingly.
 */
public class Arguments {

    /**
     * Handles the program arguments and begin the program correspondingly.
     *
     * @param args
     *            the program arguments.
     */
    public static void handleArgs(String[] args) {
        Options options = new Options();

        Option windowlessOption = new Option("w", "windowless", false,
                "Starts the program in windowless mode \n(default: disabled)");
        options.addOption(windowlessOption);

        Option fileOption = new Option("f", "file", true, "EzASM code file path to open");
        fileOption.setArgName("path");
        options.addOption(fileOption);

        Option memoryOption = new Option("m", "memory", true,
                "The number of words to allocate space for on the stack and heap each, "
                        + "must be larger than 0 (default 65536)");
        options.addOption(memoryOption);
        memoryOption.setArgName("words");

        Option wordSizeOption = new Option("s", "word-size", true, "The size in bytes of a word (default: 8)");
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

        if (commandLine.hasOption(memoryOption)) {
            String memoryString = commandLine.getOptionValue(memoryOption);
            try {
                memorySize = Integer.parseInt(memoryString);
            } catch (Exception e) {
                errorArgs("Unable to parse given word size");
            }
        } else {
            memorySize = Memory.DEFAULT_MEMORY_WORDS;
        }
        if (commandLine.hasOption(wordSizeOption)) {
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
        if (commandLine.hasOption(fileOption)) {
            filepath = commandLine.getOptionValue(fileOption);
        }

        if (commandLine.hasOption(windowlessOption)) {
            CommandLineInterface cli = null;
            if (filepath.equals("")) {
                cli = new CommandLineInterface(sim);
            } else {
                cli = new CommandLineInterface(sim, filepath);
            }
            cli.startSimulation();
        } else {
            SwingUtilities.invokeLater(() -> {
                Window.instantiate(sim);
            });
        }
    }

    /**
     * Exit the program while displaying a message.
     *
     * @param message
     *            the message to print before exiting.
     */
    private static void errorArgs(String message) {
        System.err.println(message);
        System.exit(1);
    }

}
