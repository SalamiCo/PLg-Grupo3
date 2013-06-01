package plg.gr3.vm.instr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import plg.gr3.data.BinaryOperator;
import plg.gr3.data.Operator;
import plg.gr3.data.UnaryOperator;
import plg.gr3.vm.VirtualMachine;

/**
 * Instrucción de la máquina virtual.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public abstract class Instruction {
    
    /** Opcode de la instrucción de operador */
    public static final int OPCODE_OPERATOR = 0b0000_0000;
    
    /** Máscara de bits para la instrucción de operador */
    public static final int OPMASK_OPERATOR = 0b1110_0000;
    
    /** Opcode de la instrucción push */
    public static final int OPCODE_PUSH = 0b0010_0000;
    
    /** Máscara de bits para la instrucción push */
    public static final int OPMASK_PUSH = 0b1111_1000;
    
    /** Opcode de la instrucción input */
    public static final int OPCODE_INPUT = 0b0010_1000;
    
    /** Máscara de bits para la instrucción input */
    public static final int OPMASK_INPUT = 0b1111_1000;
    
    /** Opcode de la instrucción de casting */
    public static final int OPCODE_CAST = 0b0011_0000;
    
    /** Máscara de bits para la instrucción cast */
    public static final int OPMASK_CAST = 0b1111_1000;
    
    /** Opcode de la instrucción load */
    public static final int OPCODE_LOAD = 0b0011_1000;
    
    /** Opcode de la instrucción store */
    public static final int OPCODE_STORE = 0b0011_1001;
    
    /** Opcode de la instrucción load */
    public static final int OPCODE_LOAD_IND = 0b0011_1010;
    
    /** Opcode de la instrucción store */
    public static final int OPCODE_STORE_IND = 0b0011_1011;
    
    /** Opcode de la instrucción de output */
    public static final int OPCODE_OUTPUT = 0b0011_1100;
    
    /** Opcode de la instrucción stop */
    public static final int OPCODE_STOP = 0b0011_1101;
    
    /** Opcode de la instrucción swap1 */
    public static final int OPCODE_SWAP1 = 0b0011_1110;
    
    /** Opcode de la instrucción swap2 */
    public static final int OPCODE_SWAP2 = 0b0011_1111;
    
    /** Opcode de la instrucción branch */
    public static final int OPCODE_BRANCH = 0b0100_0000;
    
    /** Máscara de bits de lainstrucción branch */
    public static final int OPMASK_BRANCH = 0b01111_1100;
    
    /** Opcode de la instrucción duplicate */
    public static final int OPCODE_DUPLICATE = 0b0100_0100;
    
    /** Opcode de la instrucción move */
    public static final int OPCODE_MOVE = 0b0100_0101;
    
    /**
     * Devuelve una instrucción de operación que usa el operador dado. Dependiendo del tipo del parámetro, el tipo
     * concreto del valor devuelto será el correspondiente, permitiéndonos crear una instrucción paracualquier operador,
     * sea del tipo que sea, de forma cómoda.
     * 
     * @param operator Operador a usar por la instrucción
     * @return Una instrucción {@link BinaryOperatorInstruction} o {@link UnaryOperatorInstruction} dependiendo del tipo
     *         del operador pasado
     * @throws IllegalArgumentException si <tt>operator</tt> no es de un tipo conocido
     */
    public static Instruction forOperator (Operator operator) {
        if (operator instanceof BinaryOperator) {
            return new BinaryOperatorInstruction((BinaryOperator) operator);
            
        } else if (operator instanceof UnaryOperator) {
            return new UnaryOperatorInstruction((UnaryOperator) operator);
            
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * @param code Código a parchear
     * @param position Posición a parchear
     * @param address Dirección de parcheo
     * @return Código parcheado
     */
    public static List<Instruction> patch (List<? extends Instruction> code, int position, int address) {
        List<Instruction> newCode = new ArrayList<>(code);
        newCode.set(position, patch(code.get(position), address));
        return Collections.unmodifiableList(newCode);
    }
    
    /**
     * @param instr Instrucción a parchear
     * @param address Dirección a parchear
     * @return Instrucción parcheada
     */
    @SuppressWarnings("unchecked")
    public static <I extends Instruction> I patch (I instr, int address) {
        if (instr instanceof JumpInstruction) {
            return (I) new JumpInstruction(address);
            
        } else if (instr instanceof BranchInstruction) {
            return (I) new BranchInstruction(address, ((BranchInstruction) instr).getConditionValue());
            
        } else {
            return instr;
        }
    }
    
    /**
     * Ejecuta esta instrucción en la máquina virtual dada
     * 
     * @param vm Máquina virtualen la que ejecutar la instrucción
     */
    public abstract void execute (VirtualMachine vm);
    
    /* package */Instruction () {
        // NOP
    }
}
