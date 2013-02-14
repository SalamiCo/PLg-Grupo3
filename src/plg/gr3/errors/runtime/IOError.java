package plg.gr3.errors.runtime;

import plg.gr3.vm.instr.Instruction;

/**
 * Error de entrada/salida.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class IOError extends RuntimeError {
    
    /**
     * @param position Posición de instrucción
     * @param instruction Instrucción implicada
     */
    public IOError (int position, Instruction instruction) {
        super(position, instruction);
    }
    
    @Override
    public String getErrorMessage () {
        return "Error de entrada/salida";
    }
    
}
