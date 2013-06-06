package plg.gr3.vm.instr;

import java.util.EmptyStackException;

import plg.gr3.data.Type;
import plg.gr3.errors.runtime.EmptyStackError;
import plg.gr3.vm.VirtualMachine;

/**
 * Clase que implementa la instruccion desapila-ind. Hereda de Instruction.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class IndirectStoreInstruction extends Instruction {

    /** Tipo de datos */
    private final Type type;

    /**
     * @param address Dirección en la que guardar el valor desapilado
     * @param type Tipo al que convertir el dato antes de almacenarlo
     */
    public IndirectStoreInstruction (Type type) {
        if (!type.isPrimitive()) {
            throw new IllegalArgumentException(type + " is not primitive");
        }
        this.type = type;
    }

    /** @return Tipo del valor en la memoria */
    public Type getType () {
        return type;
    }

    @Override
    public void execute (VirtualMachine vm) {
        int address = vm.popValue().toIntegerValue().getValue();
        try {
            vm.setMemoryValue(address, vm.popValue()); // Mem[dir] = Cima
        } catch (EmptyStackException e) {
            // Error de pila vacía
            vm.abort(new EmptyStackError(vm.getProgramCounter(), this));
        }
    }

    @Override
    public String toString () {
        return "STORE-IND(" + type.getName() + ")";
    }
}
