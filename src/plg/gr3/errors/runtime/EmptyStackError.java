package plg.gr3.errors.runtime;

import plg.gr3.vm.instr.Instruction;

public final class EmptyStackError extends RuntimeError {
    
    public EmptyStackError (int position, Instruction instruction) {
        super(position, instruction);
    }
    
    @Override
    public String getErrorMessage () {
        return "Pila vacia";
    }
    
}
