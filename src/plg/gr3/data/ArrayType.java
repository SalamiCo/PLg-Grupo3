package plg.gr3.data;

import plg.gr3.vm.instr.ConstructedType;

public final class ArrayType extends ConstructedType {
    
    private final int length;
    
    private final Type baseType;
    
    public ArrayType (Type elementType, int length) {
        super(elementType.getName() + "[" + length + "]");
        
        if (length < 0) {
            throw new IllegalArgumentException("size: " + length + " < 0");
        }
        
        this.baseType = elementType;
        this.length = length;
    }
    
    @Override
    public boolean compatible (Type type) {
        if (!(type instanceof ArrayType)) {
            return false;
        }
        ArrayType arrt = (ArrayType) type;
        
        return length == arrt.length && baseType.compatible(arrt.baseType);
    }
    
    public int getLength () {
        return length;
    }
    
    public Type getBaseType () {
        return baseType;
    }
    
    @Override
    public int getSize () {
        return baseType.getSize() * length;
    }
}
