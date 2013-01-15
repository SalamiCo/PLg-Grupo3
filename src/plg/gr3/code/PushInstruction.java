package plg.gr3.code;

import java.util.Objects;

/**
 * Clase que implementa la instruccion apila(valor). Hereda de Instruction.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class PushInstruction extends Instruction {
    
    /** Argumento de la instruccion <tt>apila(valor)</tt> */
    private final Object value;
    
    /**
     * @param value
     *            Valor que se va a apilar
     */
    public PushInstruction (Object value) {
        this.value = Objects.requireNonNull(value, "value");
    }
    
    /** @return Valor que se va a apilar */
    public Object getValue () {
        return getValue(Object.class);
    }
    
    /**
     * @param type
     *            Tipo del valor devuelto
     * @return Valor que se va a apilar convertido al tipo dado, o <tt>null</tt> si no esde ese tipo
     */
    public <T> T getValue (Class<T> type) {
        if (type.isInstance(value)) {
            return type.cast(value);
        } else {
            return null;
        }
    }
}
