package plg.gr3.symbolTable;

public class Type {
    
    public String lexeme = "";
    
    public Type (String s) {
        lexeme = s;
    }
    
    public static final Type Nat = new Type("nat"), Integer = new Type("integer"), Float = new Type("float"),
        Real = new Type("real"), Boolean = new Type("boolean"), Char = new Type("char"), Error = new Type("err");
    
}
