package plg.gr3.code;

/**
 * Clase que implementa la instruccion desapila-dir(Direccion). Hereda de Instruction.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class StoreInstruction extends Instruction {
    
    /** Dirección de memoria en la que guardar el valor */
    private final int address;
    
    /**
     * @param address
     *            Dirección en la que guardar el valor desapilado
     */
    public StoreInstruction (int address) {
        this.address = address;
    }
    
    /** @return Dirección en la que se almacenará el valor */
    public int getAddress () {
        return address;
    }
}
