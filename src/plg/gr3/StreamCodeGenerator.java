package plg.gr3;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import plg.gr3.code.BinaryOperatorInstruction;
import plg.gr3.code.Instruction;
import plg.gr3.code.UnaryOperatorInstruction;

public final class StreamCodeGenerator extends CodeGenerator {
    
    private boolean writtenHeader = false;
    
    private final DataOutputStream stream;
    
    public StreamCodeGenerator (OutputStream stream) {
        this.stream = new DataOutputStream(Objects.requireNonNull(stream, "stream"));
    }
    
    @Override
    public void generateInstruction (Instruction inst) {
        try {
            // Escribimos la cabecera si es necesario
            if (!writtenHeader) {
                stream.writeUTF("PLG-G3\0");
                writtenHeader = true;
            }
            
            // Escribimos la instrucción
            if (inst instanceof BinaryOperatorInstruction) {
                writeInst((BinaryOperatorInstruction) inst);
                
            } else if (inst instanceof UnaryOperatorInstruction) {
                writeInst((UnaryOperatorInstruction) inst);
                
            } else {
                throw new IllegalArgumentException("Unknown instruction: " + inst.getClass());
            }
        } catch (IOException exc) {
            // FIXME Lanzar una excepción decente
            throw new RuntimeException(exc);
        }
    }
    
    private void writeInst (BinaryOperatorInstruction inst) throws IOException {
        int op = Instruction.OPCODE_OPERATOR | inst.getOperator().getCode();
        stream.writeByte(op);
    }
    
    private void writeInst (UnaryOperatorInstruction inst) throws IOException {
        int op = Instruction.OPCODE_OPERATOR | inst.getOperator().getCode();
        stream.writeByte(op);
    }
    
}
