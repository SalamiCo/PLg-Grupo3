package plg.gr3.errors.compile;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import plg.gr3.lexer.OLD.LocatedToken;
import plg.gr3.lexer.OLD.Token;
import plg.gr3.lexer.OLD.TokenType;

/**
 * Identificador no esperado
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class UnexpectedTokenError extends CompileError {
    
    /** Token encontrado */
    private final Token found;
    
    /** Tipos de token que seesperaban */
    private final Set<TokenType> expected;
    
    /**
     * @param found Token encontrado
     * @param expected Tipos de token esperados
     */
    public UnexpectedTokenError (LocatedToken found, Set<TokenType> expected) {
        super(found.getLine(), found.getColumn());
        
        this.found = found.getToken();
        this.expected = Collections.unmodifiableSet(EnumSet.copyOf(expected));
    }
    
    @Override
    public String getErrorMessage () {
        final String format = "Se encontró un token %s, pero se esperaba uno de %s";
        return String.format(format, found, expected);
    }
}
