package plg.gr3.vm;

public enum BinaryOperator {
    ADDITION,
    SUBTRACTION,
    PRODUCT,
    DIVISION,
    MODULE,
    EQUALS,
    NOT_EQUALS,
    LOWER_THAN,
    LOWER_EQUAL,
    GREATER_THAN,
    GREATER_EQUALS,
    AND,
    OR;
    
    public boolean canApply (Type t1, Type t2) {
        
    }
    
    public Type getApplyType (Type t1, Type t2);
    
    public Object apply (Object o1, Object o2);
}
