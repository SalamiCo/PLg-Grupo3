package plg.gr3.data;

/**
 * Implementación del tipo <tt>natural</tt>
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class NaturalValue extends NumericValue {
    
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
    
    @Override
    public NaturalValue toNaturalValue () {
        return this;
    }
    
    @Override
    public IntegerValue toIntegerValue () {
        return IntegerValue.valueOf(value);
    }
    
    @Override
    public FloatValue toFloatValue () {
        return FloatValue.valueOf(value);
    }
    
    @Override
    public CharacterValue toCharacterValue () {
        return CharacterValue.valueOf((char) value);
    }
    
    @Override
    public NumericValue add (NumericValue other) {
        if (other instanceof NaturalValue || other instanceof IntegerValue) {
            return toIntegerValue().add(other);
            
        } else if (other instanceof FloatValue) {
            return toFloatValue().add(other);
        }
        
        throw new IllegalArgumentException(other.toString());
    }
    
    @Override
    public NumericValue subtract (NumericValue other) {
        if (other instanceof NaturalValue || other instanceof IntegerValue) {
            return toIntegerValue().subtract(other);
            
        } else if (other instanceof FloatValue) {
            return toFloatValue().subtract(other);
        }
        
        throw new IllegalArgumentException(other.toString());
    }
    
    @Override
    public NumericValue multiply (NumericValue other) {
        if (other instanceof NaturalValue) {
            int otherValue = other.toNaturalValue().getValue();
            return NaturalValue.valueOf(value * otherValue);
            
        } else if (other instanceof IntegerValue) {
            return toIntegerValue().multiply(other);
            
        } else if (other instanceof FloatValue) {
            return toFloatValue().multiply(other);
        }
        
        throw new IllegalArgumentException(other.toString());
    }
    
    @Override
    public NumericValue divide (NumericValue other) {
        if (other instanceof NaturalValue) {
            int otherValue = other.toNaturalValue().getValue();
            return NaturalValue.valueOf(value / otherValue);
            
        } else if (other instanceof IntegerValue) {
            return toIntegerValue().divide(other);
            
        } else if (other instanceof FloatValue) {
            return toFloatValue().divide(other);
        }
        
        throw new IllegalArgumentException(other.toString());
    }
    
    @Override
    public NumericValue modulo (NumericValue other) {
        if (other instanceof NaturalValue) {
            int otherValue = other.toNaturalValue().getValue();
            return NaturalValue.valueOf(value % otherValue);
        }
        
        throw new IllegalArgumentException(other.toString());
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
