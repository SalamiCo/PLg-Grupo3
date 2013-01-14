package plg.gr3;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import plg.gr3.lexer.LocatedToken;
import plg.gr3.lexer.Token;
import plg.gr3.lexer.TokenType;

/**
 * 
 * @author darkhogg
 */
public final class UnexpectedTokenError extends CompileError {
    
    /** Token encontrado */
    private final Token found;
    
    /** Tipos de token que seesperaban */
    private final Set<TokenType> expected;
    
    public UnexpectedTokenError (LocatedToken found, Set<TokenType> expected) {
        super(null, found.getLine(), found.getColumn());
        
        this.found = found.getToken();
        this.expected = Collections.unmodifiableSet(new HashSet<>(expected));
    }
    
    @Override
    public String getErrorMessage () {
        final String format = "Se encontró un token %s, pero se esperaba uno de %s";
        return String.format(format, found, expected);
    }
}
