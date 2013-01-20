package plg.gr3;

import plg.gr3.code.instructions.Instruction;

public final class TypeMissMatchError extends RuntimeError {
    
    public TypeMissMatchError (int position, Instruction instruction) {
        super(position, instruction);
    }
    
    @Override
    public String getErrorMessage () {
        // TODO Auto-generated method stub
        return "Operador mal usado";
    }
    
}
