package plg.gr3.data;

/**
 * Implementación del tipo <tt>integer</tt>
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class IntegerValue extends Value {
    
    /** Valor numérico del entero */
    private final int value;
    
    /**
     * @param value Valor numérico del entero
     */
    public IntegerValue (int value) {
        this.value = value;
    }
    
    @Override
    public Type getType () {
        return Type.INTEGER;
    }
    
    /** @return Valor nativo de este objeto */
    public int getValue () {
        return value;
    }
    
    /**
     * @param str Entero a parsear
     * @return Entero leído
     */
    public static IntegerValue valueOf (String str) {
        return IntegerValue.valueOf(Integer.parseInt(str));
    }
    
    /**
     * @param value Entero a envolver
     * @return Entero resultante de envolver <tt>value</tt>
     */
    public static IntegerValue valueOf (int value) {
        return new IntegerValue(value);
    }
    
    @Override
    public int hashCode () {
        return value;
    }
    
    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof IntegerValue)) {
            return false;
        }
        
        IntegerValue intv = (IntegerValue) obj;
        return (value == intv.value);
    }
    
    @Override
    public String toString () {
        return String.valueOf(value);
    }
}
