package plg.gr3.errors.runtime;

import plg.gr3.vm.instr.Instruction;

/**
 * Error de pila vacía.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class EmptyStackError extends RuntimeError {
    
    /**
     * @param position Posición de instrucción
     * @param instruction Instrucción implicada
     */
    public EmptyStackError (int position, Instruction instruction) {
        super(position, instruction);
    }
    
    @Override
    public String getErrorMessage () {
        return "Pila vacía";
    }
    
}
