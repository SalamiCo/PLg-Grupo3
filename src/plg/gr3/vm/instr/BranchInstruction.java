package plg.gr3.vm.instr;

import java.util.Objects;

import plg.gr3.data.BooleanValue;
import plg.gr3.vm.VirtualMachine;

public final class BranchInstruction extends Instruction {
    
    private final int address;
    
    private final BooleanValue value;
    
    public BranchInstruction (int addr, BooleanValue value) {
        this.address = addr;
        this.value = Objects.requireNonNull(value);
    }
    
    @Override
    public void execute (VirtualMachine vm) {
        if (vm.popValue().equals(value)) {
            vm.setProgramCounter(address);
        }
    }
}
