package plg.gr3.errors.runtime;

/**
 * Error de acceso a una posición de la lista de instrucciones fuera de rango.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class InvalidInstructionAddressError extends RuntimeError {
    
    /**
     * @param position Posición del acceso en el programa
     */
    public InvalidInstructionAddressError (int position) {
        super(position, null);
    }
    
    @Override
    public String getErrorMessage () {
        return String.format("Acceso a una posición inválida de memoria (#%X)", getPosition());
    }
    
}
