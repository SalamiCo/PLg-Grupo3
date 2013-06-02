package plg.gr3.vm.instr;

import plg.gr3.vm.VirtualMachine;

public final class JumpInstruction extends Instruction {
    
    private final int address;
    
    /**
     * @param address Dirección del salto
     */
    public JumpInstruction (int address) {
        this.address = address;
    }
    
    @Override
    public void execute (VirtualMachine vm) {
        vm.setProgramCounter(address);
    }
    
    @Override
    public String toString () {
        return "JUMP(0x" + Integer.toHexString(address) + ")";
    }
    
    public int getAddress () {
        return address;
    }
}
