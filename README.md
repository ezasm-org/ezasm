# EzASM - A Simpler Assembly Language

The project we propose is a small-instruction-set programming language interpreter written in Java with a GUI interface for inspecting the current state of the environment. This simple and interpreted language would be able to demonstrate the concepts of a lower level assembly language while still being easier to write. The instructions would be intuitive and simple compared to MIPS (e.g., no system calls or immediate limits) and act upon virtual registers akin to other assembly languages.

The user will be able to either run a file containing instructions (a program) or enter instructions line by line. The results of these instructions would be resultant on the GUI would list the state of all of the registers, the past/current/upcoming instructions, and the program memory.

## Implementation details and ideas:

8-byte words (64-bit integer, 64-bit float) \
Perhaps allow using 30+31 as a single 128 bit register with special instructions

Stack and Heap size: 8192 bytes (can be changed in program arguments / config)

Registers \
0-31 \
0 : ZERO \
1 : instruction line number \
2 : stack ptr \
3 : return ptr \
4-6 : arguments \
7-9 : return \
10-19 : save0 - save9 \
20-29 : temp0 - temp9 \
30-31 : special instructions 

Floating point registers \
0-9 : savef0 - savef9 \
10-19 : tempf0 - tempf9 \
20-21 : special instructions 
 

### Calling convention:
Register based similar to MIPS 

### Handling text:
First class support with embedding into data section \
C style strings (end with null byte) \
Register can have a char in it, or a pointer to a string 

### Instructions: 
Arithmetic \
add sub mul div and or xor sll srl - command (reg1/imm1) (reg2/imm2) (store register) \
not (reg1/imm1) (store register) \
inc/dec (reg1, increments or decrements the value in that register by 1) \
inc/dec (reg1) (immediate, increments or decrements the immediate) 

load/store heap-ish \
mv (reg1) (reg2) \
load (ptr) (register) \
store (ptr) (register/immediate) 

Function \
call (line number / label, equivalent to ‘jal’) \
return (0-3 registers / immediate arguments copied into return registers) 

Jump (and conditional jump) \
j(ump) (line number / label) \
jal (line number / label, puts line number in return register) \
bne beq blt bgt ble bge - command (reg1/imm1) (reg2/imm2) (line number / label) 

String manip (first argument is source, second is destination) \
strlen (ptr, stores value in return register) \
strcpy (ptr) (ptr) (second ptr buffer size) \
strcat (ptr) (ptr) \
strcat (ptr) (ptr) (number of characters) 

### System call instructions: 
alloc (register/immediate number of words) (register destination) \
alloc (register/immediate number of words, store in return register) 

printi (register/immediate, integer interpretation) \
printf (register/immediate, float interpretation) \
printc (register/immediate ASCII, char interpretation) \
prints (register/inline ptr to Cstring, string interpretation) \
prints (register/inline ptr to Cstring, string interpretation) (number of bytes to print) 

readi (register) \
readf (register) \
readc (register) \
reads (register with ptr) (size of buffer) 

exit \
exit (register/immediate return value) 

### Syntax:
Whitespace - INSTRUCTION ARG1 ARG2 … “\n” \
(and) Delimiter - INSTRUCTION ARG1, ARG2, … ; \
replace syntax with whitespace at runtime \
Dereference ptr - x86 style \
[(expr)] \
Where expr can be a register +/- an immediate \
add temp0, [temp2] \
xor [stack-8] temp2 \
Label - “LABEL_NAME:” \
Comment - “#” 
