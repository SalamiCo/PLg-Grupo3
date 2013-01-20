package plg.gr3.errors.compile;

import plg.gr3.lexer.LocatedToken;
import plg.gr3.parser.Type;

//Clase que hereda de Compile error que maneja los errores de discordancia de tipos cuando asignamos una 
//expresion con una variable. Tiene dos atributos, 
//el tipo que se ha encontrado y que no casa con lo esperado y el tipo que se esperaba.  
public final class AssignError extends CompileError {
    
    private Type typeFound;
    
    private Type typeExpected;
    
    private String identLexeme;
    
    public AssignError (Type found, Type expected, LocatedToken tokenRead) {
        super(tokenRead.getLine(), tokenRead.getColumn());
        
        this.typeFound = found;
        this.typeExpected = expected;
        this.identLexeme = tokenRead.getLexeme();
    }
    
    @Override
    public String getErrorMessage () {
        final String format = "No puedes asignar el tipo %s a la variable " + identLexeme;
        return String.format(format, typeFound, typeExpected);
    }
    
}
