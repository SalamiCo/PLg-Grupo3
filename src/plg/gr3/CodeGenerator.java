package plg.gr3;

import java.util.List;

import plg.gr3.code.Instruction;

public abstract class CodeGenerator {
    
    protected CodeGenerator () {
        // Construtor de paquete para evitar herencia externa
    }
    
    public abstract void generateInstruction (Instruction inst);
    
    public final void generateInstructions (List<Instruction> insts) {
        for (Instruction inst : insts) {
            generateInstruction(inst);
        }
    }
}
