package plg.gr3.errors.runtime;

import plg.gr3.vm.instr.Instruction;

public final class IOError extends RuntimeError {
    
    public IOError (int position, Instruction instruction) {
        super(position, instruction);
    }
    
    @Override
    public String getErrorMessage () {
        return "Error entrada/salida";
    }
    
}
