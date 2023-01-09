package com.ezasm.assembler;

import org.codehaus.plexus.util.cli.Arg;

import java.util.ArrayList;
import java.util.LinkedList;

public interface Instruction {
    LinkedList<Directive> resolve(ArrayList<Argument> args);
}
