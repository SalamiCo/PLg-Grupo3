package plg.gr3.data;

/**
 * Implementación del tipo <tt>float</tt>
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class BooleanValue extends Value {
    
    /** Valor para <tt>true</tt> */
    private final static BooleanValue TRUE = new BooleanValue(true);
    
    /** Valor para <tt>false</tt> */
    private final static BooleanValue FALSE = new BooleanValue(false);
    
    /** Valor booleano */
    private final boolean value;
    
    /**
     * @param value Valor booleano
     */
    private BooleanValue (boolean value) {
        this.value = value;
    }
    
    @Override
    public Type getType () {
        return Type.BOOLEAN;
    }
    
    /** @return Valor nativo de este objeto */
    public boolean getValue () {
        return value;
    }
    
    /**
     * @param str Flotante a parsear
     * @return Flotante leído
     */
    public static BooleanValue valueOf (String str) {
        return BooleanValue.valueOf(Boolean.parseBoolean(str));
    }
    
    /**
     * @param value Booleano que convertir
     * @return Valor booleano correspondiente
     */
    public static BooleanValue valueOf (boolean value) {
        return value ? TRUE : FALSE;
    }
    
    @Override
    public int hashCode () {
        return value ? 1 : 0;
    }
    
    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof BooleanValue)) {
            return false;
        }
        
        BooleanValue boolv = (BooleanValue) obj;
        return (value == boolv.value);
    }
    
    @Override
    public String toString () {
        return String.valueOf(value);
    }
}
