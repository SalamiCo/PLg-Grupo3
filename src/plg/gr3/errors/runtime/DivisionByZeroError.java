package plg.gr3.errors.runtime;

import plg.gr3.vm.instr.Instruction;

public final class DivisionByZeroError extends RuntimeError {
    
    public DivisionByZeroError (int position, Instruction instruction) {
        super(position, instruction);
    }
    
    @Override
    public String getErrorMessage () {
        return "Division por cero";
    }
    
}
