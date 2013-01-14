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
    
    public Type forValue(Object obj){
    	if (obj instanceof java.lang.Float )
    		return Type.FLOAT;
    	if ( obj instanceof java.lang.Integer)
    		return Type.INTEGER;
    	if (obj instanceof Natural)
    		return Type.NATURAL;
    	if (obj instanceof java.lang.Boolean)
    		return Type.BOOLEAN;
    	if (obj instanceof java.lang.Character)
    		return Type.CHARACTER;
    	return null;
    }
    
    public boolan typeMatch(Type ident, Type typeAssigned){
    	switch (ident.forValue(ident)){
    		case Type.NATURAL:
    			if (typeAssigned.equals(NATURAL))
    				return true;
    			else 
    				return false;
    		
    		break;
    		case Type.INTEGER:
    			if (typeAssigned.equals(NATURAL) || typeAssigned.equals(INTEGER)){
    				return true;
    			else
    				return false;
    			
    		break;
    		case Type.FLOAT: 
    			if (typeAssigned.equals(NATURAL) || typeAssigned.equals(INTEGER) || typeAssigned.equals(FLOAT))
    				return true;
    			else 
    				return false;
    		break;
    		default
    			return false;
    			}
    	}
    }

	public boolean isNumeric(){
		if (this.equals(NATURAL) || this.equals(INTEGER) || this.equals(FLOAT))
			return true;
		else 
			return false;
	}
}