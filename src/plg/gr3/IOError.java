package plg.gr3;

import plg.gr3.code.instructions.Instruction;

public final class IOError extends RuntimeError {
    
    public IOError (int position, Instruction instruction) {
        super(position, instruction);
    }
    
    @Override
    public String getErrorMessage () {
        return "Error entrada/salida";
    }
    
}
