package plg.gr3.data;

import plg.gr3.vm.instr.ConstructedType;

public final class ArrayType extends ConstructedType {
    
    private final Type elementType;
    
    private final int size;
    
    public ArrayType (Type elementType, int size) {
        super(elementType.getName() + "[" + size + "]");
        
        if (size < 0) {
            throw new IllegalArgumentException("size: " + size + " < 0");
        }
        
        this.elementType = elementType;
        this.size = size;
    }
    
    @Override
    public boolean compatible (Type type) {
        if (!(type instanceof ArrayType)) {
            return false;
        }
        ArrayType arrt = (ArrayType) type;
        
        return size == arrt.size && elementType.compatible(arrt.elementType);
    }
}
