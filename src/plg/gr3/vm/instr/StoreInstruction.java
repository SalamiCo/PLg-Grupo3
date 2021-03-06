package plg.gr3.vm.instr;

import java.util.EmptyStackException;

import plg.gr3.data.Type;
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
    
    /** Tipo de datos */
    private final Type type;
    
    /**
     * @param address Dirección en la que guardar el valor desapilado
     * @param type Tipo al que convertir el dato antes de almacenarlo
     */
    public StoreInstruction (int address, Type type) {
        if (address < 0) {
            throw new IllegalArgumentException("address: " + address + " < 0 ");
        }
        if (!type.isPrimitive()) {
            throw new IllegalArgumentException(type + " is not primitive");
        }
        this.address = address;
        this.type = type;
    }
    
    /** @return Dirección en la que se almacenará el valor */
    public int getAddress () {
        return address;
    }
    
    /** @return Tipo del valor enla memoria */
    public Type getType () {
        return type;
    }
    
    @Override
    public void execute (VirtualMachine vm) {
        try {
            vm.setMemoryValue(address, vm.popValue()); // Mem[dir] = Cima
        } catch (EmptyStackException e) {
            // Error de pila vacía
            vm.abort(new EmptyStackError(vm.getProgramCounter(), this));
        }
    }
    
    @Override
    public String toString () {
        return "STORE(0x" + Integer.toHexString(address) + "," + type.getName() + ")";
    }
}
