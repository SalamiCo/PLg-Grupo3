package plg.gr3.data;

/**
 * Implementación del tipo <tt>float</tt>
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class FloatValue extends NumericValue {

    /** Valor numérico del flotante */
    private final float value;

    /**
     * @param value Valor numérico del flotante
     */
    public FloatValue (float value) {
        this.value = value;
    }

    @Override
    public Type getType () {
        return Type.FLOAT;
    }

    /** @return Valor nativo de este objeto */
    public float getValue () {
        return value;
    }

    @Override
    public NaturalValue toNaturalValue () {
        return NaturalValue.valueOf((int) value);
    }

    @Override
    public IntegerValue toIntegerValue () {
        return IntegerValue.valueOf((int) value);
    }

    @Override
    public FloatValue toFloatValue () {
        return this;
    }

    @Override
    public CharacterValue toCharacterValue () {
        throw new IllegalArgumentException(toString());
    }

    @Override
    public BooleanValue toBooleanValue () {
        throw new IllegalArgumentException(toString());
    }

    @Override
    public NumericValue add (NumericValue other) {
        if (other instanceof NaturalValue || other instanceof IntegerValue || other instanceof FloatValue) {
            float otherValue = other.toFloatValue().getValue();
            return FloatValue.valueOf(value + otherValue);
        }

        throw new IllegalArgumentException(other.toString());
    }

    @Override
    public NumericValue subtract (NumericValue other) {
        if (other instanceof NaturalValue || other instanceof IntegerValue || other instanceof FloatValue) {
            float otherValue = other.toFloatValue().getValue();
            return FloatValue.valueOf(value - otherValue);
        }

        throw new IllegalArgumentException(other.toString());
    }

    @Override
    public NumericValue multiply (NumericValue other) {
        if (other instanceof NaturalValue || other instanceof IntegerValue || other instanceof FloatValue) {
            float otherValue = other.toFloatValue().getValue();
            return FloatValue.valueOf(value * otherValue);
        }

        throw new IllegalArgumentException(other.toString());
    }

    @Override
    public NumericValue divide (NumericValue other) {
        if (other instanceof NaturalValue || other instanceof IntegerValue || other instanceof FloatValue) {
            float otherValue = other.toFloatValue().getValue();
            return FloatValue.valueOf(value / otherValue);
        }

        throw new IllegalArgumentException(other.toString());
    }

    @Override
    public NumericValue modulo (NumericValue other) {
        throw new IllegalArgumentException(toString());
    }

    /**
     * @param str Flotante a parsear
     * @return Flotante leído
     */
    public static FloatValue valueOf (String str) {
        return FloatValue.valueOf(Float.parseFloat(str));
    }

    /**
     * @param value Flotante a envolver
     * @return Flotante resultante de envolver <tt>value</tt>
     */
    public static FloatValue valueOf (float value) {
        return new FloatValue(value);
    }

    @Override
    public int hashCode () {
        return Float.floatToIntBits(value);
    }

    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof FloatValue)) {
            return false;
        }

        FloatValue floatv = (FloatValue) obj;
        return (value == floatv.value);
    }

    @Override
    public String toString () {
        return String.valueOf(value);
    }
}
