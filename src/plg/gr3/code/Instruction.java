package plg.gr3.code;

import plg.gr3.BinaryOperator;

/**
 * Clase abstracta de la que heredan las instrucciones de la maquina pila
 * 
 * @author PLg Grupo 03 2012/2013
 */

public abstract class Instruction {
    
    public static Instruction forOperator (Operator op) throws IllegalArgumentException {
        if (op instanceof BinaryOperator) {
            return new BinaryOpInstruction((BinaryOperator) op);
        } else if (op instanceof UnaryOperator) {
            return new UnaryOpInstruction((UnaryOperator) op);
        } else
            throw new IllegalArgumentException();
    }
    
    /**
     * Constructor privado a nivel de paquete
     * */
    protected Instruction () {
        
    }
}
