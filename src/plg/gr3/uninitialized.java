package plg.gr3;

import plg.gr3.code.instructions.Instruction;

public final class uninitialized extends RuntimeError {
    private final int address;
    
    public uninitialized (int position, Instruction instruction, int address) {
        super(position, instruction);
        this.address = address;
    }
    
    @Override
    public String getErrorMessage () {
        String format = "La posicion de memoria %s no es valida";
        return String.format(format, address);
    }
    
}
