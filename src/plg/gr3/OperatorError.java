package plg.gr3;

import plg.gr3.lexer.LocatedToken;
import plg.gr3.parser.Type;

//Clase que hereda de Compile error que maneja los errores de tipos en operacion
//es decir, no pordemos hacer (nat+boolean;char mod float etc)

public class OperatorError extends CompileError {
    
    private Type typeA;
    
    private Type typeB;
    
    private Operator op;
    
    public OperatorError (Type typeA, Type typeB, Operator op, LocatedToken tokenRead) {
        super(tokenRead.getLine(), tokenRead.getColumn());
        
        this.typeA = typeA;
        this.typeB = typeB;
        this.op = op;
        
    }
    
}
