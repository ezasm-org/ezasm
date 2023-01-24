package com.ezasm.antlr.assembler;

import com.ezasm.antlr.assembler.elf.ELFBuilder;
import com.ezasm.antlr.assembler.parser.*;
import com.ezasm.instructions.exception.IllegalArgumentException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static com.ezasm.antlr.assembler.parser.EzASMParser.*;

/**
 * State machine implementation of an assembler. This class is only accessible through {@link Assembler#assemble(java.lang.String, FileOutputStream)}, which creates an instance to store state.
 * This class is used to convert a String containing EzASM assembler instructions into a MIPS32 executable file.
 * This class extends {@link EzASMBaseListener} and uses listener methods to assemble the instructions parsed with ANTLR.
 *
 * The overall state of the assembler is defined in {@link Assembler#sections}, which contains the data to be stored in each section of the generated executable.
 * The assembler contains public methods that can be accessed exclusively by {@link InstructionDispatcher} during the generation of {@link Directive Directives}.
 * Generated directives are parsed in a series of passes and use public {@link Assembler} methods to modify the state of the assembler as it generates the executable.
 */
public class Assembler extends EzASMBaseListener {

    /**
     * Internal state machine buffer containing all sections and the data that has been written to each section.
     * Each section's data is directly written to the corresponding section in the executable during ELF generation.
     */
    private ArrayList<List<Byte>> sections;

    /**
     * Internal state machine {@link ArrayList}<{@link Map}> containing the labels in each section and each label's offset within the section.
     */
    private ArrayList<Map<String, Integer>> labels;

    /**
     * Internal state machine component containing the current write offsets of each section in {@link #sections}.
     */
    private ArrayList<Integer> offsets;

    /**
     * Internal state machine component containing the index of the section in {@link #sections} that is currently being written to.
     */
    private int currentSection;

    /**
     * Internal state machine component containing {@link Directive Directives} for the program currently being parsed.
     */
    private LinkedList<Directive> directives;

    /**
     * Internal state machine component containing {@link Argument Arguments} for the expression currently being parsed.
     */
    private ArrayList<Argument> args;

    /**
     * Public {@link Map} containing MIPS register identifiers for each named register.
     */
    public static final Map<String, Byte> registers;

    /**
     * Internal state machine component containing the final offset of each section in {@link #sections}.
     */
    private ArrayList<Long> sectionLocations;

    /**
     * Internal {@link Map} containing defined named sections and their index in {@link #sections}.
     */
    private Map<String, Integer> sectionNames;

    /**
     * Internal {@link Map} containing defined EzASM instructions.
     */
    private static final Map<String, InstructionDispatcher> instructions;

    static {
        // Setup register mappings
        String[] temp = {"zero", "at", "v0", "v1", "a0", "a1", "a2", "a3", "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7",
                "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "t8", "t9", "k0", "k1", "gp", "sp", "fp", "ra"};

        registers = new HashMap<>();

        for (byte i = 0; i < temp.length; i++) {
            registers.put(temp[i], i);
        }


        // Define some basic instructions for testing
        instructions = new HashMap<>();

        instructions.put("add", new InstructionDispatcher(args1 ->
                new LinkedList<>(List.of((Directive)(new Directive.WriteRInstruction((byte)0b100000,
                            (byte)((Argument.Register)args1.get(1)).id(),
                            (byte)((Argument.Register)args1.get(2)).id(),
                            (byte)((Argument.Register)args1.get(0)).id(),
                            (byte)0)))
                ),
                Argument.Register.class, Argument.Register.class, Argument.Register.class));

        instructions.put("addu", new InstructionDispatcher(args1 ->
                new LinkedList<>(List.of((Directive)(new Directive.WriteRInstruction((byte)0b100001,
                        (byte)((Argument.Register)args1.get(1)).id(),
                        (byte)((Argument.Register)args1.get(2)).id(),
                        (byte)((Argument.Register)args1.get(0)).id(),
                        (byte)0)))
                ),
                Argument.Register.class, Argument.Register.class, Argument.Register.class));

        instructions.put("addi", new InstructionDispatcher(args1 ->
                new LinkedList<>(List.of((Directive)(new Directive.WriteIInstruction((byte)0b001000,
                        (byte)((Argument.Register)args1.get(1)).id(),
                        (byte)((Argument.Register)args1.get(0)).id(),
                        (char)((Argument.Immediate.Integer)args1.get(2)).value())))
                ),
                Argument.Register.class, Argument.Register.class, Argument.Immediate.Integer.class));

        instructions.put("addiu", new InstructionDispatcher(args1 ->
                new LinkedList<>(List.of((Directive)(new Directive.WriteIInstruction((byte)0b001001,
                        (byte)((Argument.Register)args1.get(1)).id(),
                        (byte)((Argument.Register)args1.get(0)).id(),
                        (char)((Argument.Immediate.Integer)args1.get(2)).value())))
                ),
                Argument.Register.class, Argument.Register.class, Argument.Immediate.Integer.class));

        instructions.put("and", new InstructionDispatcher(args1 ->
                new LinkedList<>(List.of((Directive)(new Directive.WriteRInstruction((byte)0b100100,
                        (byte)((Argument.Register)args1.get(1)).id(),
                        (byte)((Argument.Register)args1.get(2)).id(),
                        (byte)((Argument.Register)args1.get(0)).id(),
                        (byte)0)))
                ),
                Argument.Register.class, Argument.Register.class, Argument.Register.class));

        instructions.put("andi", new InstructionDispatcher(args1 ->
                new LinkedList<>(List.of((Directive)(new Directive.WriteIInstruction((byte)0b001100,
                        (byte)((Argument.Register)args1.get(1)).id(),
                        (byte)((Argument.Register)args1.get(0)).id(),
                        (char)((Argument.Immediate.Integer)args1.get(2)).value())))
                ),
                Argument.Register.class, Argument.Register.class, Argument.Immediate.Integer.class));

        instructions.put("or", new InstructionDispatcher(args1 ->
                new LinkedList<>(List.of((Directive)(new Directive.WriteRInstruction((byte)0b100101,
                        (byte)((Argument.Register)args1.get(1)).id(),
                        (byte)((Argument.Register)args1.get(2)).id(),
                        (byte)((Argument.Register)args1.get(0)).id(),
                        (byte)0)))
                ),
                Argument.Register.class, Argument.Register.class, Argument.Register.class));

        instructions.put("ori", new InstructionDispatcher(args1 ->
                new LinkedList<>(List.of((Directive)(new Directive.WriteIInstruction((byte)0b001101,
                        (byte)((Argument.Register)args1.get(1)).id(),
                        (byte)((Argument.Register)args1.get(0)).id(),
                        (char)((Argument.Immediate.Integer)args1.get(2)).value())))
                ),
                Argument.Register.class, Argument.Register.class, Argument.Immediate.Integer.class));

        instructions.put("j", new InstructionDispatcher(args1 ->
                new LinkedList<>(List.of((Directive)(new Directive.WriteJInstruction((byte)0b000010,
                        ((Argument.Immediate.Label)args1.get(0)).id())
                ))),
                Argument.Immediate.Label.class));

        instructions.put("beq", new InstructionDispatcher(args1 ->
                new LinkedList<>(List.of((Directive)(new Directive.WriteBranchInstruction((byte)0b000100,
                        (byte)((Argument.Register)args1.get(1)).id(),
                        (byte)((Argument.Register)args1.get(0)).id(),
                        ((Argument.Immediate.Label)args1.get(2)).id())))
                ),
                Argument.Register.class, Argument.Register.class, Argument.Immediate.Label.class));


        instructions.put(".section", new InstructionDispatcher(args1 ->
                new LinkedList<>(List.of((Directive)(
                        new Directive.SetSection(((Argument.Label)args1.get(0)).id())))),
                Argument.Label.class));
    }

    /**
     * Private constructor for internal use. Assembler objects should only exist within {@link #assemble(java.lang.String, FileOutputStream) assemble()}.
     */
    private Assembler() {}

    /**
     *
     * @param in the EzASM assembly code to be parsed and assembled.
     * @param out the executable file output stream.
     * @throws IOException if an I/O error occurs.
     */
    public static void assemble(String in, FileOutputStream out) throws IOException {
        // Run ANTLR lexer and parser on input string
        EzASMLexer lexer = new EzASMLexer(CharStreams.fromString(in));
        EzASMParser parser = new EzASMParser(new CommonTokenStream(lexer));
        ParseTree tree;
        try {
            tree = parser.prog();
        } catch (ParseCancellationException e) {
            System.out.println(e);
            return;
        }

        // Create assembler state machine
        Assembler assembler = new Assembler();
        ParseTreeWalker walker = new ParseTreeWalker();

        // Assemble the parsed program
        try {
            walker.walk(assembler, tree);
        } catch (AssemblerException e) {
            System.out.println(e);
            return;
        }

        // Create ELF from generated section data and write to file output stream
        ELFBuilder builder = new ELFBuilder(assembler.sections, assembler.sectionNames);
        builder.write(out);
    }

    /**
     * Creates a new section that will be added to the output executable.
     * @param name the name of the created section.
     * @return the index of the created section in {@link #sections}.
     */
    private int addSection(String name) {
        // Create section data buffer and corresponding section name entry
        sections.add(new LinkedList<>());
        sectionNames.put(name, sections.size() - 1);

        // Setup write offset and label map for new section
        labels.add(new HashMap<>());
        offsets.add(0);
        sectionLocations.add(0L);

        // Return new section's index in sections
        return sections.size() - 1;
    }

    /**
     * Writes an integer in little-endian format to the current section.
     * @param data the data to be written.
     */
    public void writeData(int data) {
        sections.get(currentSection).add((byte)(data >> 24));
        sections.get(currentSection).add((byte)(data >> 16));
        sections.get(currentSection).add((byte)(data >> 8));
        sections.get(currentSection).add((byte)(data));
    }

    /**
     * Writes an array of bytes in order to the current section.
     * @param data the data to be written.
     */
    public void writeData(byte[] data) {
        for (byte b : data) {
            sections.get(currentSection).add(b);
        }
    }

    /**
     * Sets the current section being written to, creating a new section if the specified section does not exist
     * @param name the name of the section.
     */
    public void setSection(String name) {
        if (!sectionNames.containsKey(name)) addSection(name);

        currentSection = sectionNames.get(name);
    }

    /**
     * Creates a label at the current offset within the current section.
     * @param id the identifier of the created label.
     */
    public void addLabel(String id) {
        labels.get(currentSection).put(id, offsets.get(currentSection));
    }

    public long getLabel(String id) {
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i).containsKey(id)) {
//                System.out.println(labels.get(i).get(id) + sectionLocations.get(i));
//                System.out.println(offsets.get(currentSection) + sectionLocations.get(currentSection));
                return (labels.get(i).get(id) + sectionLocations.get(i));
            }
        }
        throw new RuntimeException("Failed to resolve label '" + id + "'");
    }

    public long getLabelOffset(String id) {
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i).containsKey(id)) {
//                System.out.println("Offset: " + (labels.get(i).get(id) + sectionLocations.get(i) - (offsets.get(currentSection) + sectionLocations.get(currentSection))));
                return (labels.get(i).get(id) + sectionLocations.get(i) - (offsets.get(currentSection) + sectionLocations.get(currentSection) + 4));
            }
        }
        throw new RuntimeException("Failed to resolve label '" + id + "'");
    }

    /**
     * Parses each expression in an EzASM program and executes all generated {@link Directive Directives}.
     * @param ctx the parse tree
     */
    @Override
    public void enterProg(ProgContext ctx) {
        // Setup state machine.
        sections = new ArrayList<>();
        sectionNames = new HashMap<>();
        sectionLocations = new ArrayList<>();
        directives = new LinkedList<>();
        offsets = new ArrayList<>();
        labels = new ArrayList<>();
        currentSection = addSection("text");

        // Parse all expressions in program.
        List<ExprContext> exprs = ctx.expr();
        for (ExprContext expr : exprs) {
            enterExpr(expr);
        }


        for (Directive dir : directives) {
            // Increment section offset based on size of directive.
            // Directive size indicates how many bytes will be added to the output when the directive is executed.
            offsets.set(currentSection, offsets.get(currentSection) + dir.size());
        }

        long location = 0x10000L + 52L + 0x20L + (sections.size() + 2) * 0x28L;
        for (int i = 0; i < sections.size(); i++) {
            sectionLocations.set(i, location);
            location += (offsets.get(i) / 16L + 1L) * 16L;
        }

        // Execute all generated directives sequentially using a separate pass for each priority level.
        // Start with priority 0 and continue doing passes until all directives have been executed.
        int priority = 0;
        while (!directives.isEmpty()) {
            offsets.replaceAll(i -> 0);
            Iterator<Directive> it = directives.iterator();
            while(it.hasNext()) {
                Directive dir = it.next();

                if (dir.priority() == priority) {
                    dir.invoke(this);
                    it.remove();
                }

                // Increment section offset based on size of directive.
                // Directive size indicates how many bytes will be added to the output when the directive is executed.
                offsets.set(currentSection, offsets.get(currentSection) + dir.size());
            }

            priority++;
        }
    }

    /**
     * Parses an EzASM expression.
     * In the case of a label definition, an appropriate label is created.
     * In the case of an instruction call, all arguments are parsed, and generated {@link Directive Directives} are added to {@link #directives}.
     * All parsed {@link Argument Arguments} are added to {@link #args} before calling {@link InstructionDispatcher#execute(ArrayList)} on the parsed instruction with the parsed {@link Argument Arguments}.
     * @param ctx the parse tree
     * @throws AssemblerException if the specified instruction is undefined or if the parsed {@link Argument Arguments} do not match the instruction definition.
     */
    @Override
    public void enterExpr(ExprContext ctx) {
        // Parse based on expression type
        if (ctx.LabelDef() != null) {
            directives.add(new Directive.CreateLabel(ctx.LabelDef().getText().substring(0, ctx.LabelDef().getText().length() - 1)));
        } else {
            String instruction = ctx.Identifier().getText();

            // Ensure instruction is valid
            if (!instructions.containsKey(instruction)) {
                throw new AssemblerException(ctx.Identifier().getSymbol(), "Unknown instruction '" + instruction + "'");
            }

            // Setup args list
            args = new ArrayList<>();

            // Parse each argument
            for (ArgumentContext arg : ctx.argument()) {
                enterArgument(arg);
            }

            // Execute generated instruction call with stored arguments and add generated directives to directives
            try {
                directives.addAll(instructions.get(instruction).execute(args));
            } catch (IllegalArgumentException e) {
                if (e.getArgIndex() == -1) {
                    throw new AssemblerException(ctx.Identifier().getSymbol(), "Invalid number of arguments for instruction '" + instruction + "'");
                } else {
                    throw new AssemblerException(ctx.argument(e.getArgIndex()).start, "Invalid argument for instruction '" + instruction + "'");
                }
            }
        }
    }

    /**
     * Parses an argument within an EzASM expression context.
     * The generated {@link Argument} is added to {@link #args}.
     * @param ctx the parse tree
     * @throws AssemblerException if an {@link Argument.Register} or {@link Argument.Dereference} has an invalid register identifier, or if an {@link Argument.Dereference} has a non-integer offset.
     */
    @Override
    public void enterArgument(ArgumentContext ctx) {
        // Parse based on argument type
        if (ctx.Register() != null) {
            String reg = ctx.Register().toString().substring(1);

            // Ensure register is valid
            if (registers.containsKey(reg)) {
                args.add(new Argument.Register(registers.get(reg)));
            } else {
                throw new AssemblerException(
                        ctx.Register().getSymbol().getLine(),
                        ctx.Register().getSymbol().getCharPositionInLine(),
                        ctx.Register().getSymbol().getText() + " is not a valid register"
                );
            }

        } else if (ctx.Identifier() != null) {
            // Add identifier as label argument (this can be interpreted by the instruction dispatcher as a label or as something else).
            args.add(new Argument.Label(ctx.Identifier().getText()));
        } else if (ctx.dereference() != null) {
            String reg = ctx.dereference().Register().toString().substring(1);

            int offset = 0;

            ImmediateContext imm = ctx.dereference().immediate();

            // Parse immediate
            if (imm != null) {
                // Ensure immediate is actually an integer type
                if (imm.Decimal() != null) {
                    offset = Integer.parseInt(imm.Decimal().getText());
                } else if (imm.Hex() != null) {
                    offset = Integer.parseInt(imm.Hex().getText().substring(1, imm.Hex().getText().length() - 1), 16);
                } else {
                    throw new AssemblerException(
                            imm.getStart().getLine(),
                            imm.getStart().getCharPositionInLine(),
                            imm.getStart().getText() + " is not a valid offset"
                    );
                }
            }

            // Ensure register is valid
            if (registers.containsKey(reg)) {
                args.add(new Argument.Dereference(registers.get(reg), offset));
            } else {
                throw new AssemblerException(
                        ctx.Register().getSymbol().getLine(),
                        ctx.Register().getSymbol().getCharPositionInLine(),
                        ctx.Register().getSymbol().getText() + " is not a valid register"
                );
            }
        } else {
            // Parse immediate based on type
            if (ctx.immediate().Hex() != null) {
                args.add(new Argument.Immediate.Integer(Integer.parseInt(ctx.getText().substring(0, ctx.getText().length() - 1))));
            } else if (ctx.immediate().Decimal() != null) {
                args.add(new Argument.Immediate.Integer(Integer.parseInt(ctx.getText())));
            } else if (ctx.immediate().Char() != null) {
                args.add(new Argument.Immediate.Character(ctx.getText().charAt(1)));
            } else if (ctx.immediate().String() != null) {
                args.add(new Argument.Immediate.String(ctx.getText().substring(1, ctx.getText().length() - 1)));
            }
        }
    }
}
