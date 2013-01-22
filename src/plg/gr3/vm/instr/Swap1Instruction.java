package plg.gr3.vm.instr;

import plg.gr3.vm.VirtualMachine;

/**
 * Clase que implementa la instruccion <tt>swap1</tt>.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class Swap1Instruction extends Instruction {
    
    @Override
    public void execute (VirtualMachine vm) {
        vm.toggleSwapped1();
    }
    
}
