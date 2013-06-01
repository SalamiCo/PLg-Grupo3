package plg.gr3.code;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import plg.gr3.data.BinaryOperator;
import plg.gr3.data.BooleanValue;
import plg.gr3.data.CharacterValue;
import plg.gr3.data.FloatValue;
import plg.gr3.data.IntegerValue;
import plg.gr3.data.NaturalValue;
import plg.gr3.data.Type;
import plg.gr3.data.UnaryOperator;
import plg.gr3.vm.instr.BinaryOperatorInstruction;
import plg.gr3.vm.instr.BranchInstruction;
import plg.gr3.vm.instr.CastInstruction;
import plg.gr3.vm.instr.DuplicateInstruction;
import plg.gr3.vm.instr.IndirectLoadInstruction;
import plg.gr3.vm.instr.IndirectStoreInstruction;
import plg.gr3.vm.instr.InputInstruction;
import plg.gr3.vm.instr.Instruction;
import plg.gr3.vm.instr.JumpInstruction;
import plg.gr3.vm.instr.LoadInstruction;
import plg.gr3.vm.instr.MoveInstruction;
import plg.gr3.vm.instr.OutputInstruction;
import plg.gr3.vm.instr.PushInstruction;
import plg.gr3.vm.instr.ReturnInstruction;
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
public class StreamCodeReader extends CodeReader implements Closeable {
    
    /** Stream usado por el lector */
    private final DataInputStream stream;
    
    /**
     * @param stream Stream que usará este lector
     */
    public StreamCodeReader (InputStream stream) {
        this.stream = new DataInputStream(Objects.requireNonNull(stream, "stream"));
    }
    
    @Override
    public Instruction read () throws IOException {
        try {
            byte byteRead = stream.readByte();
            
            if ((byteRead & Instruction.OPMASK_OPERATOR) == Instruction.OPCODE_OPERATOR) {
                int code = byteRead & ~Instruction.OPMASK_OPERATOR;
                
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
                int code = byteRead & ~Instruction.OPMASK_PUSH;
                Type type = Type.forCode(code);
                
                if (type == Type.BOOLEAN) {
                    return new PushInstruction(BooleanValue.valueOf(stream.readBoolean()));
                    
                } else if (type == Type.CHARACTER) {
                    return new PushInstruction(CharacterValue.valueOf(stream.readChar()));
                    
                } else if (type == Type.NATURAL) {
                    return new PushInstruction(NaturalValue.valueOf(stream.readInt()));
                    
                } else if (type == Type.INTEGER) {
                    return new PushInstruction(IntegerValue.valueOf(stream.readInt()));
                    
                } else if (type == Type.FLOAT) {
                    return new PushInstruction(FloatValue.valueOf(stream.readFloat()));
                    
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
                int code = stream.readByte();
                return new LoadInstruction(stream.readInt(), Type.forCode(code));
                
            } else if (byteRead == Instruction.OPCODE_STORE) {
                int code = stream.readByte();
                return new StoreInstruction(stream.readInt(), Type.forCode(code));
                
            } else if (byteRead == Instruction.OPCODE_LOAD_IND) {
                int code = stream.readByte();
                return new IndirectLoadInstruction(Type.forCode(code));
                
            } else if (byteRead == Instruction.OPCODE_STORE_IND) {
                int code = stream.readByte();
                return new IndirectStoreInstruction(Type.forCode(code));
                
            } else if (byteRead == Instruction.OPCODE_OUTPUT) {
                return new OutputInstruction();
                
            } else if (byteRead == Instruction.OPCODE_STOP) {
                return new StopInstruction();
                
            } else if (byteRead == Instruction.OPCODE_SWAP1) {
                return new Swap1Instruction();
                
            } else if (byteRead == Instruction.OPCODE_SWAP2) {
                return new Swap2Instruction();
                
            } else if (byteRead == Instruction.OPCODE_DUPLICATE) {
                return new DuplicateInstruction();
                
            } else if (byteRead == Instruction.OPCODE_MOVE) {
                return new MoveInstruction(stream.readInt());
                
            } else if ((byteRead & Instruction.OPMASK_BRANCH) == Instruction.OPCODE_BRANCH) {
                int code = byteRead & ~Instruction.OPMASK_BRANCH;
                switch (code) {
                    case 0b00:
                        return new JumpInstruction(stream.readInt());
                    case 0b01:
                        return new BranchInstruction(stream.readInt(), BooleanValue.FALSE);
                    case 0b10:
                        return new BranchInstruction(stream.readInt(), BooleanValue.TRUE);
                    case 0b11:
                        return new ReturnInstruction();
                }
                
            } else {
                // El código de operador no se reconoce
                throw new IOException("Formato de bytecode inválido");
            }
        } catch (EOFException exc) {
            return null;
            
        }
        
        return null;
    }
    
    @Override
    public void close () throws IOException {
        stream.close();
    }
}
