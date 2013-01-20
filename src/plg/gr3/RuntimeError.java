package plg.gr3;

import plg.gr3.code.instructions.Instruction;

public abstract class RuntimeError {
    
    private final Instruction instruction;
    
    /* package */RuntimeError () {
        
    }
    
    public Instruction getInstruction () {
        return instruction;
    }
}
