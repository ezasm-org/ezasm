# EzASM - A Simpler Assembly Language

## Authors 
Trevor Brunette\
trevorbrunette@gmail.com

Kate Vandermolen\
CerulanLumina@users.noreply.github.com

## Building

Clone this repository somehow, either download the source from GitHub or run `git clone https://github.com/TrevorBrunette/EzASM` from the command line.

### Building from source through the command line:

Install Apache Maven \
Navigate to the EzASM source directory \
Run `mvn compile assembly:single` will build an executable .jar file in the `target` directory \
Run that jar file with `java -jar target/*.jar`

### Building using an IDE (IntelliJ IDEA):

Open IntelliJ and select open project \
Navigate to the EzASM source directory and open it \
Wait for the files to be indexed \
An SDK has to be set if one is not detected automatically (use version 17 at a minimum) \
Navigate to the `src/main/java/com/ezasm/Main.java` file \
Run that file with the play button

### Testing

**You will need to have built from the command line or IntelliJ to complete this** \
Navigate to the EzASM source directory \
Run `mvn clean test`

## Introduction

The goal of this project is to create a small-instruction-set programming language interpreter written in Java with a GUI interface for inspecting the current state of the environment. This simple interpreted language would be able to demonstrate the concepts of a lower level assembly language while still being easier to write. The instructions would be intuitive and simple compared to MIPS (e.g., no system calls or immediate limits) and act upon virtual registers akin to other assembly languages.

The user is able to either run a file containing instructions (a program) or enter instructions line by line. The results of these instructions would be resultant on the GUI would list the state of all of the registers, the past/current/upcoming instructions, and the program memory.

### A complete list of the instruction set can be found [here](https://github.com/TrevorBrunette/EzASM/wiki/Instruction-Set)
### A guide to the syntax of the language can be found [here](https://github.com/TrevorBrunette/EzASM/wiki/Syntax)
### Implementation and structure details can be found [here](https://github.com/TrevorBrunette/EzASM/wiki/Structure)


