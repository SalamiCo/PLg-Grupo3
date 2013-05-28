package plg.gr3.vm.instr;

import plg.gr3.data.Type;

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
