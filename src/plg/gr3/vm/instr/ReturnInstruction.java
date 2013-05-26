package plg.gr3.vm.instr;

import plg.gr3.vm.VirtualMachine;

public final class ReturnInstruction extends Instruction {
    
    @Override
    public void execute (VirtualMachine vm) {
        vm.setProgramCounter(vm.popValue().toIntegerValue().getValue());
    }
}
