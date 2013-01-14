package plg.gr3.code;

import plg.gr3.parser.Type;

/**
 * Clase que implementa la instruccion in(type). Hereda de Instruction.
 * 
 * @author PLg Grupo 03 2012/2013
 */

public class InOpInstruction extends Instruction {
    
    /**
     * tipo de la variable a la que se asignara el valor del buffer de entrada
     * */
    private Type typeVar;
    
    /**
     * Constructora de la instruccion de in(type)
     * */
    public InOpInstruction (Type typeVar) {
        super();
        this.typeVar = typeVar;
    }
}
