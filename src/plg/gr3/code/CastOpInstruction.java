package plg.gr3.code;

import plg.gr3.parser.Type;

/**
 * Clase que implementa las instrucciones con operadores de casting. Hereda de Instruction.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class CastOpInstruction extends Instruction {
    
    /**
     * Tipo resultante de hacer casting
     * */
    private Type castType;
    
    public CastOpInstruction (Type castType) {
        super();
        this.castType = castType;
    }
}
