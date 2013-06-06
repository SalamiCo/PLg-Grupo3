package plg.gr3.vm.instr;

import plg.gr3.vm.VirtualMachine;

/**
 * Instrucción de desechamientode operando
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class DropInstruction extends Instruction {

    @Override
    public void execute (VirtualMachine vm) {
        vm.popValue();
    }

    @Override
    public String toString () {
        return "DROP";
    }

}
