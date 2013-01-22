package plg.gr3.code;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import plg.gr3.data.BinaryOperator;
import plg.gr3.data.Natural;
import plg.gr3.data.Type;
import plg.gr3.data.UnaryOperator;
import plg.gr3.vm.instr.BinaryOperatorInstruction;
import plg.gr3.vm.instr.CastInstruction;
import plg.gr3.vm.instr.InputInstruction;
import plg.gr3.vm.instr.Instruction;
import plg.gr3.vm.instr.LoadInstruction;
import plg.gr3.vm.instr.OutputInstruction;
import plg.gr3.vm.instr.PushInstruction;
import plg.gr3.vm.instr.StopInstruction;
import plg.gr3.vm.instr.StoreInstruction;
import plg.gr3.vm.instr.Swap1Instruction;
import plg.gr3.vm.instr.Swap2Instruction;
import plg.gr3.vm.instr.UnaryOperatorInstruction;

/**
 * Lector dce código de un {@link InputStream}
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class StreamCodeReader extends CodeReader {
    
    /** Stream usado por el lector */
    private final DataInputStream stream;
    
    /**
     * @param stream
     *            Stream que usará este lector
     */
    public StreamCodeReader (InputStream stream) {
        this.stream = new DataInputStream(Objects.requireNonNull(stream, "stream"));
    }
    
    @Override
    public Instruction read () {
        try {
            
            byte byteRead = stream.readByte();
            
            if ((byteRead & Instruction.OPMASK_OPERATOR) == Instruction.OPCODE_OPERATOR) {
                int code = byteRead & ~Instruction.OPCODE_OPERATOR;
                
                // Probamos si es un operador binario
                BinaryOperator binOp = BinaryOperator.forCode(code);
                if (binOp != null) {
                    return new BinaryOperatorInstruction(binOp);
                }
                
                // Probamos si es un operador binario
                UnaryOperator unOp = UnaryOperator.forCode(code);
                if (unOp != null) {
                    return new UnaryOperatorInstruction(unOp);
                }
                
                // El código de operador no se reconoce
                throw new IOException("Formato de bytecode inválido");
                
            } else if ((byteRead & Instruction.OPMASK_PUSH) == Instruction.OPCODE_PUSH) {
                int code = byteRead & ~Instruction.OPCODE_PUSH;
                Type type = Type.forValue(code);
                
                if (type == Type.BOOLEAN) {
                    return new PushInstruction(Boolean.valueOf(stream.readBoolean()));
                    
                } else if (type == Type.CHARACTER) {
                    return new PushInstruction(Character.valueOf(stream.readChar()));
                    
                } else if (type == Type.NATURAL) {
                    return new PushInstruction(new Natural(stream.readInt()));
                    
                } else if (type == Type.INTEGER) {
                    return new PushInstruction(Integer.valueOf(stream.readInt()));
                    
                } else if (type == Type.FLOAT) {
                    return new PushInstruction(Float.valueOf(stream.readFloat()));
                    
                } else {
                    throw new IOException("Formato de bytecode inválido");
                }
                
            } else if ((byteRead & Instruction.OPMASK_INPUT) == Instruction.OPCODE_INPUT) {
                int code = byteRead & ~Instruction.OPCODE_INPUT;
                return new InputInstruction(Type.forCode(code));
                
            } else if ((byteRead & Instruction.OPMASK_CAST) == Instruction.OPCODE_CAST) {
                int code = byteRead & ~Instruction.OPCODE_CAST;
                return new CastInstruction(Type.forCode(code));
                
            } else if (byteRead == Instruction.OPCODE_LOAD) {
                return new LoadInstruction(stream.readInt());
                
            } else if (byteRead == Instruction.OPCODE_STORE) {
                return new StoreInstruction(stream.readInt());
                
            } else if (byteRead == Instruction.OPCODE_OUTPUT) {
                return new OutputInstruction();
                
            } else if (byteRead == Instruction.OPCODE_STOP) {
                return new StopInstruction();
                
            } else if (byteRead == Instruction.OPCODE_SWAP1) {
                return new Swap1Instruction();
                
            } else if (byteRead == Instruction.OPCODE_SWAP2) {
                return new Swap2Instruction();
                
            } else {
                // El código de operador no se reconoce
                throw new IOException("Formato de bytecode inválido");
            }
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }
}
