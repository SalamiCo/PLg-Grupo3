package plg.gr3;

import java.util.List;

import com.sun.org.apache.bcel.internal.generic.Instruction;

public abstract class CodeGenerator {
    
    protected CodeGenerator () {
        
    }
    
    public abstract void generateInstruction (Instruction inst);
    
    public final void generateInstructions (List<Instruction> insts) {
        for (Instruction inst : insts) {
            generateInstruction(inst);
        }
    }
}
