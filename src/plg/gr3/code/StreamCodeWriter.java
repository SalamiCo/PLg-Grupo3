package plg.gr3.code;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import plg.gr3.Natural;
import plg.gr3.code.instructions.BinaryOperatorInstruction;
import plg.gr3.code.instructions.CastInstruction;
import plg.gr3.code.instructions.InputInstruction;
import plg.gr3.code.instructions.Instruction;
import plg.gr3.code.instructions.LoadInstruction;
import plg.gr3.code.instructions.OutputInstruction;
import plg.gr3.code.instructions.PushInstruction;
import plg.gr3.code.instructions.StopInstruction;
import plg.gr3.code.instructions.StoreInstruction;
import plg.gr3.code.instructions.Swap1Instruction;
import plg.gr3.code.instructions.Swap2Instruction;
import plg.gr3.code.instructions.UnaryOperatorInstruction;
import plg.gr3.parser.Type;

/**
 * Generador de código que escribirá el código a un {@link OutputStream}
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class StreamCodeWriter extends CodeWriter {
    
    /** Si la cabecera del fichero se ha escrita */
    private boolean writtenHeader = false;
    
    /** Si el generador está inhibido */
    private boolean inhibited = false;
    
    /** Stream al que se escribirá el código */
    private final DataOutputStream stream;
    
    /**
     * @param stream
     *            Stream a usar para la generación de código
     */
    public StreamCodeWriter (OutputStream stream) {
        this.stream = new DataOutputStream(Objects.requireNonNull(stream, "stream"));
    }
    
    @Override
    public void write (Instruction inst) {
        if (!inhibited) {
            try {
                // Escribimos la cabecera si es necesario
                if (!writtenHeader) {
                    stream.writeUTF("PLG-G3\0");
                    writtenHeader = true;
                }
                
                // Escribimos la instrucción
                if (inst instanceof BinaryOperatorInstruction) {
                    writeBinaryOp((BinaryOperatorInstruction) inst);
                    
                } else if (inst instanceof UnaryOperatorInstruction) {
                    writeUnaryOp((UnaryOperatorInstruction) inst);
                    
                } else if (inst instanceof CastInstruction) {
                    writeCast((CastInstruction) inst);
                    
                } else if (inst instanceof LoadInstruction) {
                    writeLoad((LoadInstruction) inst);
                    
                } else if (inst instanceof StoreInstruction) {
                    writeStore((StoreInstruction) inst);
                    
                } else if (inst instanceof InputInstruction) {
                    writeInput((InputInstruction) inst);
                    
                } else if (inst instanceof OutputInstruction) {
                    writeOutput((OutputInstruction) inst);
                    
                } else if (inst instanceof PushInstruction) {
                    writePush((PushInstruction) inst);
                    
                } else if (inst instanceof Swap1Instruction) {
                    writeSwap1((Swap1Instruction) inst);
                    
                } else if (inst instanceof Swap2Instruction) {
                    writeSwap2((Swap2Instruction) inst);
                    
                } else if (inst instanceof StopInstruction) {
                    writeStop((StopInstruction) inst);
                    
                } else {
                    throw new IllegalArgumentException("Unknown instruction: " + inst.getClass());
                }
            } catch (IOException exc) {
                // FIXME Lanzar una excepción decente
                throw new RuntimeException(exc);
            }
        }
    }
    
    @Override
    public void inhibit () {
        inhibited = true;
    }
    
    private void writeStop (StopInstruction inst) throws IOException {
        stream.writeByte(Instruction.OPCODE_STOP);
    }
    
    private void writeSwap1 (Swap1Instruction inst) throws IOException {
        stream.writeByte(Instruction.OPCODE_SWAP1);
    }
    
    private void writeSwap2 (Swap2Instruction inst) throws IOException {
        stream.writeByte(Instruction.OPCODE_SWAP2);
    }
    
    private void writePush (PushInstruction inst) throws IOException {
        Object value = inst.getValue();
        Type type = Type.forValue(value);
        
        stream.writeByte(Instruction.OPCODE_PUSH | (type.getCode() & 0b111));
        writeValue(value);
    }
    
    private void writeOutput (OutputInstruction inst) throws IOException {
        stream.writeByte(Instruction.OPCODE_OUTPUT);
    }
    
    private void writeInput (InputInstruction inst) throws IOException {
        stream.writeByte(Instruction.OPCODE_INPUT | inst.getInputType().getCode());
    }
    
    private void writeStore (StoreInstruction inst) throws IOException {
        stream.writeByte(Instruction.OPCODE_STORE);
        stream.writeInt(inst.getAddress());
    }
    
    private void writeLoad (LoadInstruction inst) throws IOException {
        stream.writeByte(Instruction.OPCODE_STORE);
        stream.writeInt(inst.getAddress());
    }
    
    private void writeCast (CastInstruction inst) throws IOException {
        stream.writeByte(Instruction.OPCODE_CAST | inst.getCastType().getCode());
    }
    
    private void writeBinaryOp (BinaryOperatorInstruction inst) throws IOException {
        int op = Instruction.OPCODE_OPERATOR | inst.getOperator().getCode();
        stream.writeByte(op);
    }
    
    private void writeUnaryOp (UnaryOperatorInstruction inst) throws IOException {
        int op = Instruction.OPCODE_OPERATOR | inst.getOperator().getCode();
        stream.writeByte(op);
    }
    
    private void writeValue (Object value) throws IOException {
        if (value instanceof Natural) {
            stream.writeInt(((Natural) value).intValue());
            
        } else if (value instanceof Integer) {
            stream.writeInt(((Integer) value).intValue());
            
        } else if (value instanceof Float) {
            stream.writeFloat(((Float) value).floatValue());
            
        } else if (value instanceof Character) {
            stream.writeChar(((Character) value).charValue());
            
        } else if (value instanceof Boolean) {
            stream.writeBoolean(((Boolean) value).booleanValue());
            
        } else {
            throw new IllegalArgumentException(value.toString());
        }
    }
}
