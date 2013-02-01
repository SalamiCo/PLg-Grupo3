package plg.gr3.vm.instr;

import java.util.Objects;

import plg.gr3.data.Value;
import plg.gr3.vm.VirtualMachine;

/**
 * Clase que implementa la instruccion apila(valor). Hereda de Instruction.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class PushInstruction extends Instruction {
    
    /** Argumento de la instruccion <tt>apila(valor)</tt> */
    private final Value value;
    
    /**
     * @param value Valor que se va a apilar
     */
    public PushInstruction (Value value) {
        this.value = Objects.requireNonNull(value, "value");
    }
    
    /** @return Valor que se va a apilar */
    public Value getValue () {
        return getValue(Value.class);
    }
    
    /**
     * @param type Tipo del valor devuelto
     * @return Valor que se va a apilar convertido al tipo dado, o <tt>null</tt> si no esde ese tipo
     */
    public <T extends Value> T getValue (Class<T> type) {
        if (type.isInstance(value)) {
            return type.cast(value);
        } else {
            return null;
        }
    }
    
    @Override
    public void execute (VirtualMachine vm) {
        vm.pushValue(value);
    }
}
