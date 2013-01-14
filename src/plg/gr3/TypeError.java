package plg.gr3;

import plg.gr3.lexer.LocatedToken;
import plg.gr3.parser.Type;

//Clase que hereda de Compile error que maneja los errores de discordancia de tipos. Tiene dos atributos, 
//el tipo que se ha encontrado y que no casa con lo esperado y el tipo que se esperaba.  
public final class TypeError extends CompileError {
    
    private Type typeFound;
    
    private Type typeExpected;
    
    public TypeError (Type found, Type expected, LocatedToken tokenRead) {
        super(tokenRead.getLine(), tokenRead.getColumn());
        
        this.typeFound = found;
        this.typeExpected = expected;
    }
    
    @Override
    public String getErrorMessage () {
        final String format = "Se encontró el tipo %s, pero se esperaba el tipo %s";
        return String.format(format, typeFound, typeExpected);
    }
    
}
