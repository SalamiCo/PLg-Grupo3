package plg.gr3.lexer.OLD;

import java.util.Objects;

/**
 * Clase que representa la ubicacion de un Token en el fichero.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class LocatedToken {
    
    /** Token al que se hace referencia */
    private final Token token;
    
    /** Linea donde se ubica el token en el fichero */
    private final int line;
    
    /** Columna donde se ubica el token en el fichero */
    private final int column;
    
    /**
     * Constructora parametrizada de la clase LocatedToken
     * 
     * @param token Token al que se hace referencia. Debe ser distinto de null.
     * @param line Linea donde se ubica el token en el fichero. Debe ser mayor que 0.
     * @param column Columna donde se ubica el token en el fichero. Debe ser mayor que 0.
     * @throws IllegalArgumentException Si alguna de las precondiciones no se cumple.
     * 
     * */
    public LocatedToken (Token token, int line, int column) throws IllegalArgumentException {
        if (line <= 0) {
            throw new IllegalArgumentException("line: " + line + " <= 0");
        }
        if (column <= 0) {
            throw new IllegalArgumentException("column: " + column + " <= 0");
        }
        
        this.token = Objects.requireNonNull(token, "token");
        this.line = line;
        this.column = column;
    }
    
    /** @return Token al que se hace referencia */
    public Token getToken () {
        return token;
    }
    
    /** @return Tipo del token */
    public TokenType getType () {
        return token.getType();
    }
    
    /** @return Lexema del token */
    public String getLexeme () {
        return token.getLexeme();
    }
    
    /** @return Linea donde se ubica el token en el fichero */
    public int getLine () {
        return line;
    }
    
    /** @return Columna donde se ubica el token en el fichero */
    public int getColumn () {
        return column;
    }
    
    @Override
    public int hashCode () {
        return Objects.hash(token, line, column);
    }
    
    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof LocatedToken)) {
            return false;
        }
        
        LocatedToken lt = (LocatedToken) obj;
        return Objects.equals(lt.token, token) && lt.line == line && lt.column == column;
    }
    
    @Override
    public String toString () {
        return "[" + token.toString() + ", " + line + ", " + column + "]";
    }
}
