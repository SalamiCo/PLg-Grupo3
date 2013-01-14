package plg.gr3.code;

/**
 * Clase que implementa la instruccion desapila-dir(Direccion). Hereda de Instruction.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class StoreInstruction extends Instruction {
    
    /**
     * direccion de memoria de la instruccion desapila-dir
     * */
    private int dir;
    
    public StoreInstruction (int dir) {
        super();
        this.dir = dir;
    }
}
