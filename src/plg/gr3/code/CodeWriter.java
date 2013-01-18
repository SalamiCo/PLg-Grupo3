package plg.gr3.code;

import java.util.List;

import plg.gr3.code.instructions.Instruction;

public abstract class CodeWriter {
    
    protected CodeWriter () {
        // Construtor de paquete para evitar herencia externa
    }
    
    public abstract void write (Instruction inst);
    
    public final void write (List<Instruction> insts) {
        for (Instruction inst : insts) {
            write(inst);
        }
    }
    
    public abstract void inhibit ();
}
