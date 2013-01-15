package plg.gr3.code;

import plg.gr3.parser.Type;

/**
 * Clase que implementa la instruccion <tt>in(type)</tt>.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class InputInstruction extends Instruction {
    
    /** Tipo de la variable a la que se asigna el valor del buffer de entrada */
    private Type inputType;
    
    /**
     * @param inputType
     *            Tipo del valor que se leerá
     */
    public InputInstruction (Type inputType) {
        this.inputType = inputType;
    }
    
    /** @return Tipo del valor que se leerá */
    public Type getInputType () {
        return inputType;
    }
}
