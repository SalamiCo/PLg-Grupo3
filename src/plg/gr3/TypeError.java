package plg.gr3;

import plg.gr3.lexer.LocatedToken;
import plg.gr3.lexer.TokenType;

//Clase que hereda de Compile error que maneja los errores de discordancia de tipos. Tiene dos atributos, 
//el tipo que se ha encontrado y que no casa con lo esperado y el tipo que se esperaba.  
public final class TypeError extends CompileError {
    
    private TokenType typeFound;
    
    private TokenType typeExpected;
    
    public TypeError (LocatedToken found, LocatedToken expected) {
        super(found.getLine(), found.getColumn());
        
        this.typeFound = found.getToken().getType();
        this.typeExpected = expected.getToken().getType();
    }
    
    @Override
    public String getErrorMessage () {
        final String format = "Se encontró el tipo %s, pero se esperaba el tipo %s";
        return String.format(format, typeFound, typeExpected);
    }
    
}
