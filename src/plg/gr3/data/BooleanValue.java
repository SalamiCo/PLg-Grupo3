package plg.gr3.data;

/**
 * Implementación del tipo <tt>float</tt>
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class BooleanValue extends Value {

    /** Valor para <tt>true</tt> */
    public final static BooleanValue TRUE = new BooleanValue(true);

    /** Valor para <tt>false</tt> */
    public final static BooleanValue FALSE = new BooleanValue(false);

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

    @Override
    public NaturalValue toNaturalValue () {
        throw new IllegalArgumentException(toString());
    }

    @Override
    public IntegerValue toIntegerValue () {
        throw new IllegalArgumentException(toString());
    }

    @Override
    public FloatValue toFloatValue () {
        throw new IllegalArgumentException(toString());
    }

    @Override
    public BooleanValue toBooleanValue () {
        return this;
    }

    @Override
    public CharacterValue toCharacterValue () {
        throw new IllegalArgumentException(toString());
    }

    @Override
    public int compare (Value other) {
        if (other instanceof BooleanValue) {
            boolean otherValue = ((BooleanValue) other).getValue();
            return value ? (otherValue ? 0 : 1) : (otherValue ? -1 : 0);
        }

        throw new IllegalArgumentException(other.toString());
    }
}
