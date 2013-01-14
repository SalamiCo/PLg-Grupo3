package plg.gr3.code;

/**
 * Clase que implementa la instruccion desapila-dir(Direccion). Hereda de Instruction.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class InstructionStore extends Instruction {
    
    /**
     * direccion de memoria de la instruccion desapila-dir
     * */
    private int dir;
    
    public InstructionStore (int dir) {
        super();
        this.dir = dir;
    }
}
