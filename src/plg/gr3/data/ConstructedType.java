package plg.gr3.data;


public abstract class ConstructedType extends Type {
    
    protected ConstructedType (String name) {
        super(name);
    }
    
    @Override
    public boolean isNumeric () {
        return false;
    }
    
    @Override
    public boolean isPrimitive () {
        return false;
    }
    
}
