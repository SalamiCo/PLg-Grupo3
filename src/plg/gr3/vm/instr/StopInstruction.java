package plg.gr3.vm.instr;

import plg.gr3.vm.VirtualMachine;

/**
 * Clase que implementa la instruccion <tt>stop</tt>.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class StopInstruction extends Instruction {
    
    @Override
    public void execute (VirtualMachine vm) {
        vm.stop();
    }
    
    @Override
    public String toString () {
        return "STOP";
    }
    
}
