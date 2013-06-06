package plg.gr3.vm.instr;

import plg.gr3.vm.VirtualMachine;

/**
 * Instrucción de salto por operando de pila.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class ReturnInstruction extends Instruction {

    @Override
    public void execute (VirtualMachine vm) {
        vm.setProgramCounter(vm.popValue().toIntegerValue().getValue());
    }

    @Override
    public String toString () {
        return "RETURN";
    }
}
