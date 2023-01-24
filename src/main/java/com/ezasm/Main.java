package com.ezasm;

import com.ezasm.simulation.Simulator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.ezasm.Arguments.handleArgs;

/**
 * The main entrypoint of the program.
 */
public class Main {

    /**
     * The main function entrypoint of the program
     * @param args the program arguments.
     */
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        handleArgs(args);
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