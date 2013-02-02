package plg.gr3.data;

/**
 * Implementación del tipo <tt>integer</tt>
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class IntegerValue extends NumericValue {
    
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
    
    @Override
    public NaturalValue toNaturalValue () {
        return NaturalValue.valueOf(value);
    }
    
    @Override
    public IntegerValue toIntegerValue () {
        return this;
    }
    
    @Override
    public FloatValue toFloatValue () {
        return FloatValue.valueOf(value);
    }
    
    @Override
    public CharacterValue toCharacterValue () {
        throw new IllegalArgumentException(toString());
    }
    
    @Override
    public NumericValue add (NumericValue other) {
        if (other instanceof NaturalValue || other instanceof IntegerValue) {
            int otherValue = other.toIntegerValue().getValue();
            return IntegerValue.valueOf(value + otherValue);
            
        } else if (other instanceof FloatValue) {
            return toFloatValue().add(other);
        }
        
        throw new IllegalArgumentException(other.toString());
    }
    
    @Override
    public NumericValue subtract (NumericValue other) {
        if (other instanceof NaturalValue || other instanceof IntegerValue) {
            int otherValue = other.toIntegerValue().getValue();
            return IntegerValue.valueOf(value - otherValue);
            
        } else if (other instanceof FloatValue) {
            return toFloatValue().subtract(other);
        }
        
        throw new IllegalArgumentException(other.toString());
    }
    
    @Override
    public NumericValue multiply (NumericValue other) {
        if (other instanceof NaturalValue || other instanceof IntegerValue) {
            int otherValue = other.toIntegerValue().getValue();
            return IntegerValue.valueOf(value * otherValue);
            
        } else if (other instanceof FloatValue) {
            return toFloatValue().multiply(other);
        }
        
        throw new IllegalArgumentException(other.toString());
    }
    
    @Override
    public NumericValue divide (NumericValue other) {
        if (other instanceof NaturalValue || other instanceof IntegerValue) {
            int otherValue = other.toIntegerValue().getValue();
            return IntegerValue.valueOf(value / otherValue);
            
        } else if (other instanceof FloatValue) {
            return toFloatValue().divide(other);
        }
        
        throw new IllegalArgumentException(other.toString());
    }
    
    @Override
    public NumericValue modulo (NumericValue other) {
        if (other instanceof NaturalValue) {
            int otherValue = other.toNaturalValue().getValue();
            return IntegerValue.valueOf(value % otherValue);
        }
        
        throw new IllegalArgumentException(other.toString());
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
