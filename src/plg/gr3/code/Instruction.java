package plg.gr3.code;

import plg.gr3.BinaryOperator;
import plg.gr3.Operator;
import plg.gr3.UnaryOperator;

/**
 * Instrucción de la máquina virtual.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public abstract class Instruction {
    
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
    
    /* package */Instruction () {
        // NOP
    }
}
