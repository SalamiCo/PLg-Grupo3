package plg.gr3.vm.instr;

import plg.gr3.vm.VirtualMachine;

/**
 * Clase que implementa la instruccion <tt>swap2</tt>.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class Swap2Instruction extends Instruction {
    
    @Override
    public void execute (VirtualMachine vm) {
        vm.toggleSwapped2();
    }
    
    @Override
    public String toString () {
        return "SWAP2";
    }
    
}
