package com.ezasm.antlr.assembler;

import java.util.ArrayList;
import java.util.LinkedList;

public interface Instruction {
    LinkedList<Directive> resolve(ArrayList<Argument> args);
}
