package plg.gr3;

import plg.gr3.code.instructions.Instruction;

public final class EmptyStackError extends RuntimeError {
    
    public EmptyStackError (int position, Instruction instruction) {
        super(position, instruction);
    }
    
    @Override
    public String getErrorMessage () {
        return "Pila vacia";
    }
    
}
