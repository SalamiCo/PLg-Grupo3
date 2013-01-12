package plg.gr3.parser;

import java.util.Objects;

public final class Type {
    
    public static final Type NATURAL = new Type("natural");
    
    public static final Type INTEGER = new Type("integer");
    
    public static final Type FLOAT = new Type("float");
    
    public static final Type BOOLEAN = new Type("boolean");
    
    public static final Type CHARACTER = new Type("character");
    
    public static final Type ERROR = new Type("");
    
    public final String name;
    
    public Type (String name) {
        this.name = Objects.requireNonNull(name, "name");
    }
    
    public String getName () {
        return name;
    }
    
    @Override
    public int hashCode () {
        return Objects.hashCode(name);
    }
    
    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof Type)) {
            return false;
        }
        
        Type type = (Type) obj;
        return name.equals(type.name);
    }
    
    @Override
    public String toString () {
        return "Type(" + name + ")";
    }
}
