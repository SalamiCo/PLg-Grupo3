package plg.gr3.data;

/**
 * Implementación del tipo <tt>natural</tt>
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class NaturalValue extends Value {
    
    /** Valor numérico del natural */
    private final int value;
    
    /**
     * @param value Valor numérico del natural
     * @throws IllegalArgumentException si el valor pasado es negativo
     */
    private NaturalValue (int value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        
        this.value = value;
    }
    
    @Override
    public Type getType () {
        return Type.NATURAL;
    }
    
    /** @return Valor nativo de este objeto */
    public int getValue () {
        return value;
    }
    
    /**
     * @param str Natural a parsear
     * @return Natural leído
     */
    public static NaturalValue valueOf (String str) {
        int i = Integer.parseInt(str);
        if (i < 0) {
            throw new NumberFormatException(str);
        }
        
        return NaturalValue.valueOf(i);
    }
    
    /**
     * @param value Entero a envolver
     * @return Natural resultante de envolver <tt>value</tt>
     */
    public static NaturalValue valueOf (int value) {
        return new NaturalValue(value);
    }
    
    @Override
    public int hashCode () {
        return value;
    }
    
    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof NaturalValue)) {
            return false;
        }
        
        NaturalValue natv = (NaturalValue) obj;
        return (value == natv.value);
    }
    
    @Override
    public String toString () {
        return String.valueOf(value);
    }
}
