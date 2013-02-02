package plg.gr3.vm.instr;

import plg.gr3.data.Value;
import plg.gr3.errors.runtime.UninitializedError;
import plg.gr3.vm.VirtualMachine;

/**
 * Clase que implementa la instruccion <tt>apila-dir(dir)</tt>.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class LoadInstruction extends Instruction {
    
    /** Dirección de memoria que se carga */
    private final int address;
    
    /**
     * @param address Dirección de memoria de la carga
     */
    public LoadInstruction (int address) {
        if (address < 0) {
            throw new IllegalArgumentException("address: " + address + " < 0 ");
        }
        this.address = address;
    }
    
    /** @return Dirección de memoria de la carga */
    public int getAddress () {
        return address;
    }
    
    @Override
    public void execute (VirtualMachine vm) {
        Value value = vm.getMemoryValue(address);
        if (value != null) {
            vm.pushValue(value); // Cima = Mem[dir]
            
        } else {
            vm.abort(new UninitializedError(vm.getProgramCounter(), this, address));
        }
    }
}
