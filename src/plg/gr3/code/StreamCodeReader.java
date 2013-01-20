package plg.gr3.code;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import plg.gr3.BinaryOperator;
import plg.gr3.UnaryOperator;
import plg.gr3.code.instructions.BinaryOperatorInstruction;
import plg.gr3.code.instructions.Instruction;
import plg.gr3.code.instructions.LoadInstruction;
import plg.gr3.code.instructions.OutputInstruction;
import plg.gr3.code.instructions.StopInstruction;
import plg.gr3.code.instructions.StoreInstruction;
import plg.gr3.code.instructions.Swap1Instruction;
import plg.gr3.code.instructions.Swap2Instruction;
import plg.gr3.code.instructions.UnaryOperatorInstruction;

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
    public Instruction read () throws IOException {
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
            
        } else if ((byteRead & Instruction.OPMASK_INPUT) == Instruction.OPCODE_INPUT) {
            
        } else if ((byteRead & Instruction.OPMASK_CAST) == Instruction.OPCODE_CAST) {
            
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
        }
    }
}
