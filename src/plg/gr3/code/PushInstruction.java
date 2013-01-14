package plg.gr3.code;

/**
 * Clase que implementa la instruccion apila(valor). Hereda de Instruction.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class PushInstruction extends Instruction {
    
    /**
     * Argumento de la instruccion apila(valor), puede ser de cualquier tipo.
     * */
    private Object arg;
    
    /**
     * Constructora de apila(valor).
     * 
     * @param arg
     *            Argumento de la instruccion apila(valor).
     * */
    public PushInstruction (Object arg) {
        super();
        this.arg = arg;
    }
}
