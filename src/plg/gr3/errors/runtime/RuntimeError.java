package plg.gr3.errors.runtime;

import plg.gr3.debug.Debugger;
import plg.gr3.errors.Error;
import plg.gr3.vm.instr.Instruction;

/**
 * Clase que representa un error en la ejecución de un programa.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public abstract class RuntimeError extends Error {
    
    /** Posición del programa en la que dió el error */
    private final int position;
    
    /** Instrucción que dio el error */
    private final Instruction instruction;
    
    /**
     * @param position Posición del programa en la que dio el error
     * @param instruction Instrucción que dio el error
     */
    /* package */RuntimeError (int position, Instruction instruction) {
        this.position = position;
        this.instruction = instruction;
    }
    
    /** @return Posición en la que se dio el error */
    public final int getPosition () {
        return position;
    }
    
    /** @return Instrucción que dio el error */
    public final Instruction getInstruction () {
        return instruction;
    }
    
    @Override
    public final void print () {
        Debugger.INSTANCE.in(position, instruction).error("%s", getErrorMessage());
    }
}
