package plg.gr3.code.instructions;

import plg.gr3.BinaryOperator;
import plg.gr3.Operator;
import plg.gr3.UnaryOperator;
import plg.gr3.vm.VirtualMachine;

/**
 * Instrucción de la máquina virtual.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public abstract class Instruction {
    
    /** Opcode de la instrucción de operador */
    public static final int OPCODE_OPERATOR = 0b0000_0000;
    
    /** Opcode de la instrucción push */
    public static final int OPCODE_PUSH = 0b0010_0000;
    
    /** Opcode de la instrucción input */
    public static final int OPCODE_INPUT = 0b0010_1000;
    
    /** Opcode de la instrucción de casting */
    public static final int OPCODE_CAST = 0b0011_0000;
    
    /** Opcode de la instrucción load */
    public static final int OPCODE_LOAD = 0b0011_1000;
    
    /** Opcode de la instrucción store */
    public static final int OPCODE_STORE = 0b0011_1001;
    
    /** Opcode de la instrucción de output */
    public static final int OPCODE_OUTPUT = 0b0011_1010;
    
    /** Opcode de la instrucción stop */
    public static final int OPCODE_STOP = 0b0011_1011;
    
    /** Opcode de la instrucción swap1 */
    public static final int OPCODE_SWAP1 = 0b0011_1100;
    
    /** Opcode de la instrucción swap2 */
    public static final int OPCODE_SWAP2 = 0b0011_1101;
    
    /**
     * Devuelve una instrucción de operación que usa el operador dado. Dependiendo del tipo del parámetro, el tipo
     * concreto del valor devuelto será el correspondiente, permitiéndonos crear una instrucción paracualquier operador,
     * sea del tipo que sea, de forma cómoda.
     * 
     * @param operator
     *            Operador a usar por la instrucción
     * @return Una instrucción {@link BinaryOperatorInstruction} o {@link UnaryOperatorInstruction} dependiendo del tipo
     *         del operador pasado
     * @throws IllegalArgumentException
     *             si <tt>operator</tt> no es de un tipo conocido
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
     * Ejecuta esta instrucción en la máquina virtual dada
     * 
     * @param vm
     *            Máquina virtualen la que ejecutar la instrucción
     */
    public abstract void execute (VirtualMachine vm);
    
    /* package */Instruction () {
        // NOP
    }
}
