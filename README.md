# EzASM

### An assembly-like programming language for use in education

## Introduction

The goal of this project is to create a small-instruction-set programming language interpreter written in Java with a GUI interface for inspecting the current state of the environment. This simple interpreted language would be able to demonstrate the concepts of a lower level assembly language while still being easier to write. The instructions would be intuitive and simple compared to MIPS (e.g., no system calls or immediate limits) and act upon virtual registers akin to other assembly languages.

The user is able to either run a file containing instructions (a program) or enter instructions line by line. The results of these instructions would be resultant on the GUI would list the state of all of the registers, the past/current/upcoming instructions, and the program memory.

## Installation

The latest release can be found at the [EzASM releases repository](https://github.com/ezasm-org/EzASM-releases/releases/latest) to install it manually.

For MacOS/Linux, EzASM can also be downloaded via [homebrew](https://brew.sh/) the [EzASM tap](https://github.com/ezasm-org/homebrew-ezasm):

```
$ brew tap ezasm-org/ezasm
$ brew install ezasm
```

## Building 

[Java SDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and  [Maven](https://maven.apache.org/install.html) are required to build EzASM.

You can use the following Maven install commands for your OS/distribution: 

#### macOS: 
```
    $ brew install maven
    $ sdk install maven
    $ sudo port install maven3
```

#### Linux:
```
    $ sudo apt install maven
    $ sudo dnf install maven
    $ sudo yum install maven
```

#### Windows:
```
    $ choco install maven
    $ scoop install main/maven
```

You can confirm that maven has been installed using `mvn -v`

### To build EzASM from the command line:

- clone the EzASM repository locally `$ git clone https://github.com/ezasm-org/EzASM.git`
- navigate to the repository's root directory `$ cd /ezasm`
- run this command to build the project using Maven:`$ mvn clean compile assembly:single`
- run this command to run your build:`$ java -jar target/*full.jar`

### To run EzASM from IDE:

Navigate to and open the EzASM project root directory
Navigate to the project's Main.java file `(\ezasm\src\main\java\com\ezasm\Main.java)`

### Testing

**You will need to have built from the command line or IntelliJ to complete this** \
Navigate to the EzASM project root directory \
Run `$ mvn clean test`

### Building packaged executables:

#### Instructions

Navigate to the EzASM project root directory \
Run `$ mvn clean package` will build a variety of native executables in the `target` directory \
Options marked with an asterisk `*` will only be generated if you fulfill the requirements to install:
being on the required operating system and having the necessary optional dependencies.


#### The appendix can be found [here](https://github.com/ezasm-org/EzASM/wiki/Instruction-Set)
#### A guide to the syntax of the language can be found [here](https://github.com/ezasm-org/EzASM/wiki/Syntax)
#### Implementation and structure details can be found [here](https://github.com/ezasm-org/EzASM/wiki/Structure)

