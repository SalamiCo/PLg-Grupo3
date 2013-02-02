package plg.gr3.data;

/**
 * Implementación del tipo <tt>float</tt>
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class CharacterValue extends Value {
    
    /** Valor del caracter */
    private final char value;
    
    /**
     * @param value Valor del caracter
     */
    public CharacterValue (char value) {
        this.value = value;
    }
    
    @Override
    public Type getType () {
        return Type.CHARACTER;
    }
    
    /** @return Valor nativo de este objeto */
    public char getValue () {
        return value;
    }
    
    /**
     * @param str Caracter a parsear
     * @return Caracter leído
     */
    public static CharacterValue valueOf (String str) {
        if (str.length() == 1) {
            return CharacterValue.valueOf(str.charAt(0));
            
        } else if (str.matches("'.'")) {
            return CharacterValue.valueOf(str.charAt(1));
            
        } else {
            throw new IllegalArgumentException(str);
        }
    }
    
    /**
     * @param value Caracter a envolver
     * @return Caracter resultante de envolver <tt>value</tt>
     */
    public static CharacterValue valueOf (char value) {
        return new CharacterValue(value);
    }
    
    @Override
    public int hashCode () {
        return value;
    }
    
    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof CharacterValue)) {
            return false;
        }
        
        CharacterValue charv = (CharacterValue) obj;
        return (value == charv.value);
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
    public CharacterValue toCharacterValue () {
        return this;
    }
    
    @Override
    public int compare (Value other) {
        char otherValue = other.toCharacterValue().getValue();
        return value - otherValue;
    }
}
