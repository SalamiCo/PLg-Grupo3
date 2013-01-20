package plg.gr3;

import plg.gr3.code.instructions.Instruction;

public final class DivisionByZero extends RuntimeError {
    
    public DivisionByZero (int position, Instruction instruction) {
        super(position, instruction);
    }
    
    @Override
    public String getErrorMessage () {
        return "Division por cero";
    }
    
}
