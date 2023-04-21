package com.ezasm.instructions.implementation;

import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.instructions.targets.inputoutput.VectorInputOutput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.TransformationSequence;
import com.ezasm.simulation.transform.transformable.InputOutputTransformable;
import com.ezasm.util.RawData;
import jdk.incubator.vector.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class VectorInstructions {

    private final Simulator simulator;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public VectorInstructions(Simulator simulator) {
        this.simulator = simulator;
    }

    /**
     * Template (vector, vector) -> vector arithmetic operation.
     *
     * @param op     operation to apply to the arguments.
     * @param output the output of the operation.
     * @param input1 the left-hand side of the operation.
     * @param input2 the right-hand side of the operation.
     */
    private TransformationSequence vectorBinary(VectorOperators.Binary op, VectorInputOutput output,
                                                    VectorInputOutput input1, VectorInputOutput input2) throws SimulationException {

        ByteBuffer data1 = ByteBuffer.wrap(input1.get(simulator).data()).order(ByteOrder.nativeOrder());
        ByteBuffer data2 = ByteBuffer.wrap(input2.get(simulator).data()).order(ByteOrder.nativeOrder());

        ByteBuffer dataOut = ByteBuffer.wrap(new byte[Memory.wordSize() * VectorInputOutput.WORD_LENGTH]).order(ByteOrder.nativeOrder());

        if (Memory.wordSize() == 4) {
            VectorSpecies<Integer> species = IntVector.SPECIES_256;

            IntVector in1 = IntVector.fromByteBuffer(species, data1, 0, ByteOrder.nativeOrder());
            IntVector in2 = IntVector.fromByteBuffer(species, data2, 0, ByteOrder.nativeOrder());
            IntVector res = in1.lanewise(op, in2);

            res.intoByteBuffer(dataOut, 0, ByteOrder.nativeOrder());

            InputOutputTransformable io = new InputOutputTransformable(simulator, output);
            return new TransformationSequence(io.transformation(new RawData(dataOut.array())));
        } else if (Memory.wordSize() == 8) {
            VectorSpecies<Long> species = LongVector.SPECIES_256;

            LongVector in1 = LongVector.fromByteBuffer(species, data1, 0, ByteOrder.nativeOrder());
            LongVector in2 = LongVector.fromByteBuffer(species, data2, 0, ByteOrder.nativeOrder());
            LongVector res = in1.lanewise(op, in2);

            res.intoByteBuffer(dataOut, 0, ByteOrder.nativeOrder());

            InputOutputTransformable io = new InputOutputTransformable(simulator, output);
            return new TransformationSequence(io.transformation(new RawData(dataOut.array())));
        } else {
            return null;
        }
    }

    /**
     * Template vector reduction operation.
     *
     * @param op     operation to apply to the arguments.
     * @param output the output of the operation.
     * @param input  the input of the operation.
     */
    private TransformationSequence vectorAssociative(VectorOperators.Associative op, IAbstractInputOutput output,
                                                    VectorInputOutput input) throws SimulationException {

        ByteBuffer data = ByteBuffer.wrap(input.get(simulator).data()).order(ByteOrder.nativeOrder());

        if (Memory.wordSize() == 4) {
            VectorSpecies<Integer> species = IntVector.SPECIES_256;

            IntVector in = IntVector.fromByteBuffer(species, data, 0, ByteOrder.nativeOrder());
            int res = in.reduceLanes(op);

            System.out.println(in.toString());

            InputOutputTransformable io = new InputOutputTransformable(simulator, output);
            return new TransformationSequence(io.transformation(new RawData(res)));
        } else if (Memory.wordSize() == 8) {
            VectorSpecies<Long> species = LongVector.SPECIES_512;

            LongVector in = LongVector.fromByteBuffer(species, data, 0, ByteOrder.nativeOrder());
            long res = in.reduceLanes(op);

            InputOutputTransformable io = new InputOutputTransformable(simulator, output);
            return new TransformationSequence(io.transformation(new RawData(res)));
        } else {
            return null;
        }
    }

    /**
     * The standard add operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the addition operation.
     * @param input2 the right-hand side of the addition operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence addv(VectorInputOutput output, VectorInputOutput input1, VectorInputOutput input2)
            throws SimulationException {
        return vectorBinary(VectorOperators.ADD, output, input1, input2);
    }

    /**
     * The standard subtract operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the addition operation.
     * @param input2 the right-hand side of the addition operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence subv(VectorInputOutput output, VectorInputOutput input1, VectorInputOutput input2)
            throws SimulationException {
        return vectorBinary(VectorOperators.ADD, output, input1, input2);
    }

    /**
     * The standard sum operation.
     *
     * @param output the output of the operation.
     * @param input the vector to sum over.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence sumv(IAbstractInputOutput output, VectorInputOutput input)
            throws SimulationException {
        return vectorAssociative(VectorOperators.ADD, output, input);
    }

    /**
     * The standard fill operation.
     *
     * @param output the vector to fill.
     * @param input the value to fill the vector with.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence fillv(VectorInputOutput output, IAbstractInput input)
            throws SimulationException {

        ByteBuffer res = ByteBuffer.allocate(VectorInputOutput.WORD_LENGTH * Memory.wordSize()).order(ByteOrder.nativeOrder());

        if (Memory.wordSize() == 4) {
            int[] data = new int[VectorInputOutput.WORD_LENGTH];
            Arrays.fill(data, (int)input.get(simulator).intValue());

            res.asIntBuffer().put(data);
        } else if (Memory.wordSize() == 8) {
            long[] data = new long[VectorInputOutput.WORD_LENGTH];
            Arrays.fill(data, input.get(simulator).intValue());

            res.asLongBuffer().put(data);
        }

        System.out.println(Arrays.toString(res.array()));

        InputOutputTransformable io = new InputOutputTransformable(simulator, output);
        return new TransformationSequence(io.transformation(new RawData(res.array())));
    }
}
