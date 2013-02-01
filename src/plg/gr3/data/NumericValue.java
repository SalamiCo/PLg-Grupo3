package plg.gr3.data;

public abstract class NumericValue {
    
    public abstract NumericValue add (NumericValue other);
    
    public abstract NumericValue subtract (NumericValue other);
    
    public abstract NumericValue multiply (NumericValue other);
    
    public abstract NumericValue divide (NumericValue other);
    
    public abstract NumericValue modulo (NumericValue other);
    
    public abstract int compare (NumericValue other);
}
