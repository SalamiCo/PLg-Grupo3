package plg.gr3.vm.instr;

import plg.gr3.data.Type;
import plg.gr3.data.Value;
import plg.gr3.errors.runtime.UninitializedMemoryError;
import plg.gr3.vm.VirtualMachine;

/**
 * Clase que implementa la instruccion <tt>apila-dir(dir)</tt>.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class LoadInstruction extends Instruction {
    
    /** Dirección de memoria que se carga */
    private final int address;
    
    /** Tipo de datos */
    private final Type type;
    
    /**
     * @param address Dirección de memoria de la carga
     * @param type Tipo al que convertir el dato antes de apilarlo
     */
    public LoadInstruction (int address, Type type) {
        if (address < 0) {
            throw new IllegalArgumentException("address: " + address + " < 0 ");
        }
        if (!type.isPrimitive()) {
            throw new IllegalArgumentException(type + " is not primitive");
        }
        this.address = address;
        this.type = type;
    }
    
    /** @return Dirección de memoria de la carga */
    public int getAddress () {
        return address;
    }
    
    /** @return Tipo del valor enla memoria */
    public Type getType () {
        return type;
    }
    
    @Override
    public void execute (VirtualMachine vm) {
        Value value = vm.getMemoryValue(address);
        if (value != null) {
            vm.pushValue(value.castTo(type)); // Cima = Mem[dir]
            
        } else {
            vm.abort(new UninitializedMemoryError(vm.getProgramCounter(), this, address));
        }
    }
    
    @Override
    public String toString () {
        return "LOAD(0x" + Integer.toHexString(address) + "," + type.getName() + ")";
    }
}
