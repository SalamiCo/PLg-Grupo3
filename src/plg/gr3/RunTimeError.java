package plg.gr3;

import plg.gr3.code.instructions.Instruction;

public abstract class RunTimeError {
    
    private Instruction instructionError;
    
    public RunTimeError () {
        
    }
    
    public Instruction getInstruction () {
        return instructionError;
    }
    
    public void setInstructionError (Instruction instruction) {
        instructionError = instruction;
    }
}
