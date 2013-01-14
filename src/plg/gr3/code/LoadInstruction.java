package plg.gr3.code;

/**
 * Clase que implementa la instruccion apila-dir(Direccion). Hereda de Instruction.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class LoadInstruction extends Instruction {
    
    /**
     * direccion de memoria de la instruccion apila-dir
     * */
    private int dir;
    
    /**
     * Constructor de la instruccion apila-dir(Direccion)
     * */
    public LoadInstruction (int dir) {
        super();
        this.dir = dir;
    }
}
