package plg.gr3;

import plg.gr3.code.instructions.Instruction;

public final class NegativeNaturalError extends RuntimeError {
    
    public NegativeNaturalError (int position, Instruction instruction) {
        super(position, instruction);
    }
    
    @Override
    public String getErrorMessage () {
        return "No se puede asignar un numero negativo a un natural";
    }
    
}
