package plg.gr3.code;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import plg.gr3.code.instructions.BinaryOperatorInstruction;
import plg.gr3.code.instructions.Instruction;
import plg.gr3.code.instructions.UnaryOperatorInstruction;

/**
 * Generador de código que escribirá el código a un {@link OutputStream}
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class StreamCodeGenerator extends CodeWriter {
    
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
    public StreamCodeGenerator (OutputStream stream) {
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
    }
    
    @Override
    public void inhibit () {
        inhibited = true;
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
