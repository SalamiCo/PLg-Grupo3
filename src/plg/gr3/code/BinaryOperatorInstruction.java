package plg.gr3.code;

import plg.gr3.BinaryOperator;

/**
 * Clase que engloba todas las operaciones binarias.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class BinaryOperatorInstruction extends Instruction {
    
    /** Operador binario de la instruccion de la maquina pila */
    private BinaryOperator operator;
    
    /**
     * @param operator
     *            Operador usado por esta instrucción
     */
    public BinaryOperatorInstruction (BinaryOperator operator) {
        this.operator = operator;
    }
    
    /** @return Operador usado por la instrucción */
    public BinaryOperator getOperator () {
        return operator;
    }
}
