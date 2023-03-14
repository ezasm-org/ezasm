# EzASM

### An assembly-like programming language for use in education

Releases can be found at the [EzASM releases repository](https://github.com/ezasm-org/EzASM-releases). 

## Introduction

The goal of this project is to create a small-instruction-set programming language interpreter written in Java with a GUI interface for inspecting the current state of the environment. This simple interpreted language would be able to demonstrate the concepts of a lower level assembly language while still being easier to write. The instructions would be intuitive and simple compared to MIPS (e.g., no system calls or immediate limits) and act upon virtual registers akin to other assembly languages.

The user is able to either run a file containing instructions (a program) or enter instructions line by line. The results of these instructions would be resultant on the GUI would list the state of all of the registers, the past/current/upcoming instructions, and the program memory.


## Building

[Java SDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) is required to build EzASM.

Clone this repository, either download the source from GitHub or run `git clone https://github.com/ezasm-org/EzASM.git` from the command line.

### Building from source through the command line:

#### Requirements:

- [Java 17 SDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Apache Maven](https://maven.apache.org/index.html)

#### Instructions

Navigate to the EzASM source directory \
Run `mvn clean compile assembly:single` will build an executable .jar file in the `target` directory \
Run that jar file with `java -jar target/*full.jar`

### Building using an IDE (IntelliJ IDEA):

#### Requirements:

- [Java 17 SDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [IntelliJ Idea](https://www.jetbrains.com/idea/)

#### Instructions:

Open IntelliJ and select open project \
Navigate to the EzASM source directory and open it \
A Java 17 SDK has to be set in `File -> Project Structure -> Project -> SDK` if the IDE does not detect it automatically \
Navigate to and open the `src/main/java/com/ezasm/Main.java` file \
Wait for the files to be indexed \
Run that file with either play button that appears in the line number gutter

### Testing

**You will need to have built from the command line or IntelliJ to complete this** \
Navigate to the EzASM source directory \
Run `mvn clean test`

### Building packaged executables:

#### Requirements:

- [Java 17 SDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Apache Maven](https://maven.apache.org/index.html)

- *Windows optionally to generate native Windows installation files
- *[InnoSetup](https://github.com/fvarrui/JavaPackager/blob/master/docs/windows-tools-guide.md) optionally to generate native Windows installation files
- *Linux optionally to generate native linux installation files
- *[appstreamcli](https://man.archlinux.org/man/appstreamcli.1.en) and [FUSE](https://wiki.archlinux.org/title/FUSE) optionally for AppImage support
- *Mac OSX optionally to generate native Mac OSX installation files

#### Instructions

Navigate to the EzASM source directory \
Run `mvn clean package` will build a variety of native executables in the `target` directory \
Options marked with an asterisk `*` will only be generated if you fulfill the requirements to install:
being on the required operating system and having the necessary optional dependencies.
- `EzASM-VERSION-full.jar` the distributable jar file
- `EzASM-VERSION-windows.zip` the zipped program which will run on Windows natively
- `EzASM-VERSION-mac.tar.gz` the archived program which will run on Mac OS X natively
- `EzASM-VERSION-linux.tar.gz` the archived program which will run on many linux distros natively
- `EzASM_VERSION.exe`* a Windows installation file
- `EzASM_VERSION.dmg`* a Mac OSX installation file
- `EzASM_VERSION.pkg`* a Mac OSX installation file
- `EzASM_VERSION.deb`* an installation file for debian-based linux distributions (Ubuntu, Mint, etc.)
- `EzASM_VERSION.rpm`* an installation file for distributions with the Red Hat package manager (Fedora, Red Hat, etc.)


### A complete list of the instruction set can be found [here](https://github.com/ezasm-org/EzASM/wiki/Instruction-Set)
### A guide to the syntax of the language can be found [here](https://github.com/ezasm-org/EzASM/wiki/Syntax)
### Implementation and structure details can be found [here](https://github.com/ezasm-org/EzASM/wiki/Structure)

