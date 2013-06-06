package plg.gr3.data;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import plg.gr3.Util;

public final class TupleType extends ConstructedType {

    private final List<Type> subtypes;

    public TupleType (List<Type> types) {
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

    public List<Type> getSubtypes () {
        return subtypes;
    }

    @Override
    public int getSize () {
        Iterator<Type> it = subtypes.iterator();
        int length = 0;
        while (it.hasNext()) {
            length += it.next().getSize();
        }
        return length;
    }

    public int getOffset (int numElement) {
        if (numElement < 0 || numElement > getSize()) {
            throw new IllegalArgumentException("numElement: " + numElement);
        } else if (numElement == 0) {
            return 0;
        } else {
            Iterator<Type> it = subtypes.iterator();
            int offset = 0;
            int count = 0;
            while (count < numElement && it.hasNext()) {
                offset += it.next().getSize();
                count++;
            }
            return offset;
        }
    }
}
