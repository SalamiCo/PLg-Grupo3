package plg.gr3.vm.instr;

import plg.gr3.vm.VirtualMachine;

/**
 * Instrucción de duplicado de la cima de la pila.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class DuplicateInstruction extends Instruction {
    
    @Override
    public void execute (VirtualMachine vm) {
        vm.pushValue(vm.peekValue());
    }
    
}
