package plg.gr3.errors.compile;

import plg.gr3.data.Type;
import plg.gr3.lexer.LocatedToken;

/**
 * Error de tipos en la asignación
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class AssignationTypeError extends CompileError {
    
    private Type found;
    
    private Type expected;
    
    private String ident;
    
    /**
     * @param found Tipo encontrado en la expresión
     * @param expected Tipo esperado por el identificador
     * @param token Token del identificador
     */
    public AssignationTypeError (Type found, Type expected, LocatedToken token) {
        super(token.getLine(), token.getColumn());
        
        this.found = found;
        this.expected = expected;
        this.ident = token.getLexeme();
    }
    
    @Override
    public String getErrorMessage () {
        String format = "El tipo '%s' no es asignable al identificador '%s' de tipo '%s'";
        return String.format(format, found, ident, expected);
    }
    
}
