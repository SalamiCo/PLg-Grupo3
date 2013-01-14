package plg.gr3.code;

import plg.gr3.BinaryOperator;

/**
 * Clase que engloba todas las operaciones binarias. Hereda de Instruction.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class BinaryOpInstruction extends Instruction {
    
    /**
     * Operador binario de la instruccion de la maquina pila
     * */
    private BinaryOperator op;
    
    /**
     * Constructor de la instruccion con operador binario
     * 
     * @param op
     *            operador binario
     * */
    public BinaryOpInstruction (BinaryOperator op) {
        /*
         * Se supone que el operador es binarioen Instruction existe un metodo estatico forOperatorque crea una
         * InstructionBinaryOp si el operador es binario
         */
        super();
        this.op = op;
    }
}
