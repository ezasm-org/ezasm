package com.ezasm;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.ezasm.Arguments.handleArgs;

/**
 * The main entrypoint of the program.
 */
public class Main {

    /**
     * The main function entrypoint of the program
     *
     * @param args the program arguments.
     */
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        handleArgs(args);
    }

}
