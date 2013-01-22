package plg.gr3.vm.instr;

import java.util.EmptyStackException;

import plg.gr3.errors.runtime.EmptyStackError;
import plg.gr3.vm.VirtualMachine;

/**
 * Clase que implementa la instruccion desapila-dir(Direccion). Hereda de Instruction.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class StoreInstruction extends Instruction {
    
    /** Dirección de memoria en la que guardar el valor */
    private final int address;
    
    /**
     * @param address
     *            Dirección en la que guardar el valor desapilado
     */
    public StoreInstruction (int address) {
        this.address = address;
    }
    
    /** @return Dirección en la que se almacenará el valor */
    public int getAddress () {
        return address;
    }
    
    @Override
    public void execute (VirtualMachine vm) {
        try {
            vm.setMemoryValue(address, vm.popValue()); //Mem[dir] = Cima  
        } catch (EmptyStackException e) {
            //Error de pila vacía
            vm.abort(new EmptyStackError(vm.getProgramCounter(), this));
        }
    }
}
