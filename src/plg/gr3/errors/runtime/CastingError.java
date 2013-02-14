package plg.gr3.errors.runtime;

import plg.gr3.data.Type;
import plg.gr3.data.Value;
import plg.gr3.vm.instr.Instruction;

public final class CastingError extends RuntimeError {
    
    private final Value value;
    
    private final Type type;
    
    public CastingError (int position, Instruction instruction, Value value, Type type) {
        super(position, instruction);
        
        this.value = value;
        this.type = type;
    }
    
    @Override
    public String getErrorMessage () {
        return String.format("No se pudo convertir %s al tipo %s", value, type);
    }
    
}
