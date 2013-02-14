package plg.gr3.errors.runtime;

import plg.gr3.vm.instr.Instruction;

/**
 * Error de memoria sin inicializar.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class UninitializedMemoryError extends RuntimeError {
    
    private final int address;
    
    /**
     * @param position Posición de instrucción
     * @param instruction Instrucción implicada
     * @param address Dirección de memoria implicada
     */
    public UninitializedMemoryError (int position, Instruction instruction, int address) {
        super(position, instruction);
        this.address = address;
    }
    
    @Override
    public String getErrorMessage () {
        String format = "La posición de memoria 0x%X no está inicializada";
        return String.format(format, address);
    }
    
}
