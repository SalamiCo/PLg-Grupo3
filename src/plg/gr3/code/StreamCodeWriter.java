package plg.gr3.code;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import plg.gr3.code.instructions.BinaryOperatorInstruction;
import plg.gr3.code.instructions.CastInstruction;
import plg.gr3.code.instructions.InputInstruction;
import plg.gr3.code.instructions.Instruction;
import plg.gr3.code.instructions.LoadInstruction;
import plg.gr3.code.instructions.OutputInstruction;
import plg.gr3.code.instructions.PushInstruction;
import plg.gr3.code.instructions.StoreInstruction;
import plg.gr3.code.instructions.Swap1Instruction;
import plg.gr3.code.instructions.Swap2Instruction;
import plg.gr3.code.instructions.UnaryOperatorInstruction;

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
                    
                } else {
                    throw new IllegalArgumentException("Unknown instruction: " + inst.getClass());
                }
            } catch (IOException exc) {
                // FIXME Lanzar una excepción decente
                throw new RuntimeException(exc);
            }
        }
    }
    
    private void writeSwap1 (Swap1Instruction inst) {
        stream.writeByte(Instruction.OPCODE_SWAP1);
    }
    
    private void writeSwap2 (Swap2Instruction inst) {
        stream.writeByte(Instruction.OPCODE_SWAP2);
    }
    
    private void writePush (PushInstruction inst) {
        // TODO Auto-generated method stub
        
    }
    
    private void writeOutput (OutputInstruction inst) {
        // TODO Auto-generated method stub
        
    }
    
    private void writeInput (InputInstruction inst) {
        // TODO Auto-generated method stub
        
    }
    
    private void writeStore (StoreInstruction inst) {
        // TODO Auto-generated method stub
        
    }
    
    private void writeLoad (LoadInstruction inst) {
        // TODO Auto-generated method stub
        
    }
    
    private void writeCast (CastInstruction inst) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void inhibit () {
        inhibited = true;
    }
    
    private void writeBinaryOp (BinaryOperatorInstruction inst) throws IOException {
        int op = Instruction.OPCODE_OPERATOR | inst.getOperator().getCode();
        stream.writeByte(op);
    }
    
    private void writeUnaryOp (UnaryOperatorInstruction inst) throws IOException {
        int op = Instruction.OPCODE_OPERATOR | inst.getOperator().getCode();
        stream.writeByte(op);
    }
}
