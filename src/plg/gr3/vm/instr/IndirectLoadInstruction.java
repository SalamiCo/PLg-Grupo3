package plg.gr3.vm.instr;

import plg.gr3.data.Type;
import plg.gr3.data.Value;
import plg.gr3.errors.runtime.UninitializedMemoryError;
import plg.gr3.vm.VirtualMachine;

/**
 * Clase que implementa la instruccion <tt>apila-ind</tt>.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class IndirectLoadInstruction extends Instruction {

    /** Tipo de datos */
    private final Type type;

    /**
     * @param type Tipo al que convertir el dato antes de apilarlo
     */
    public IndirectLoadInstruction (Type type) {
        if (!type.isPrimitive()) {
            throw new IllegalArgumentException(type + " is not primitive");
        }
        this.type = type;
    }

    /** @return Tipo del valor enla memoria */
    public Type getType () {
        return type;
    }

    @Override
    public void execute (VirtualMachine vm) {
        int address = vm.popValue().toIntegerValue().getValue();
        Value value = vm.getMemoryValue(address);
        if (value != null) {
            vm.pushValue(value.castTo(type)); // Cima = Mem[dir]

        } else {
            vm.abort(new UninitializedMemoryError(vm.getProgramCounter(), this, address));
        }
    }

    @Override
    public String toString () {
        return "LOAD-IND(" + type.getName() + ")";
    }
}
