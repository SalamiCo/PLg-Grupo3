package plg.gr3.code;

/**
 * Clase que implementa la instruccion <tt>apila-dir(dir)</tt>.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class LoadInstruction extends Instruction {
    
    /** Dirección de memoria que se carga */
    private final int address;
    
    /**
     * @param address
     *            Dirección de memoria de la carga
     */
    public LoadInstruction (int address) {
        if (address < 0) {
            throw new IllegalArgumentException("address: " + address + " < 0 ");
        }
        this.address = address;
    }
    
    /** @return Dirección de memoria de la carga */
    public int getAddress () {
        return address;
    }
}
