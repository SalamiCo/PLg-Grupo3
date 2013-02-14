package plg.gr3.errors.runtime;

import plg.gr3.vm.instr.Instruction;

/**
 * Error de división por cero
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class DivisionByZeroError extends RuntimeError {
    
    /**
     * @param position Posición de instrucción
     * @param instruction Instrucción implicada
     */
    public DivisionByZeroError (int position, Instruction instruction) {
        super(position, instruction);
    }
    
    @Override
    public String getErrorMessage () {
        return "División por cero";
    }
    
}
