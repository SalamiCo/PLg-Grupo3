package plg.gr3.data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import plg.gr3.Util;
import plg.gr3.vm.instr.ConstructedType;

public final class TupleType extends ConstructedType {
    
    private final List<Type> subtypes;
    
    protected TupleType (List<Type> types) {
        super("(" + Util.join(",", Objects.requireNonNull(types, "types")) + ")");
        
        if (types.contains(null)) {
            throw new NullPointerException("types");
        }
        
        subtypes = Collections.unmodifiableList(types);
    }
    
    @Override
    public boolean compatible (Type type) {
        if (!(type instanceof TupleType)) {
            return false;
        }
        TupleType tupt = (TupleType) type;
        
        if (subtypes.size() != tupt.subtypes.size()) {
            return false;
        }
        for (int i = 0; i < subtypes.size(); i++) {
            if (!subtypes.get(i).compatible(tupt.subtypes.get(i))) {
                return false;
            }
        }
        return true;
    }
}
