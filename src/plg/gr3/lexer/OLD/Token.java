package plg.gr3.lexer.OLD;

import java.util.Objects;

/**
 * Clase que representa un token o componente léxico del lenguaje.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class Token {
    
    /** Categoria lexica del Token */
    private final TokenType type;
    
    /** Lexema del Token */
    private final String lexeme;
    
    /**
     * @param type
     *            Categoría láxica del Token
     * @param lexeme
     *            Lexema del Token
     * @throws IllegalArgumentException
     *             Si el lexema no encaja con su categoría léxica
     */
    public Token (TokenType type, String lexeme) throws IllegalArgumentException {
        this.type = Objects.requireNonNull(type, "type");
        this.lexeme = Objects.requireNonNull(lexeme, "lexeme");
        
        if (!type.matches(lexeme)) {
            throw new IllegalArgumentException();
        }
        
    }
    
    /** @return Categoría léxica de este token */
    public TokenType getType () {
        return type;
    }
    
    /** @return Lexema de este token */
    public String getLexeme () {
        return lexeme;
    }
    
    @Override
    public int hashCode () {
        return Objects.hash(type, lexeme);
    }
    
    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof Token)) {
            return false;
        }
        
        Token t = (Token) obj;
        return Objects.equals(t.type, type) && Objects.equals(t.lexeme, lexeme);
    }
    
    @Override
    public String toString () {
        if (type == TokenType.EOF) {
            return type.toString();
        } else {
            return type + "(\"" + lexeme + "\")";
        }
    }
}
