package plg.gr3.lexer;

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
        if (type.getPattern().matcher(lexeme).matches()) {
            this.type = type;
            this.lexeme = lexeme;
        } else {
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
}
