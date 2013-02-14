package plg.gr3.errors.compile;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import plg.gr3.lexer.TokenType;

/**
 * Error de token sin reconocer
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class UnrecognizedTokenError extends CompileError {
    
    /** Token encontrado */
    private final String found;
    
    private final Set<TokenType> expected;
    
    /**
     * @param found Token encontrado
     * @param line Línea en la que se encontrón
     * @param column Columna en la que se encontró
     * @param expected Tokens esperados
     */
    public UnrecognizedTokenError (String found, int line, int column, Set<TokenType> expected) {
        super(line, column);
        
        this.found = found;
        this.expected = Collections.unmodifiableSet(new HashSet<>(expected));
    }
    
    @Override
    public String getErrorMessage () {
        String format = "Se encontró un token no reconocido '%s', esperaba uno de %s";
        return String.format(format, found, expected);
    }
}
