package plg.gr3.lexer;

/**
 * Clase que representa un token o componente lexico del lenguaje.
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
     *            Categoria lexica del Token
     * @param lexeme
     *            Lexema del Token
     * @throws IllegalArgumentException
     *             Si el lexema no encaja con su categoria lexica
     */
    public Token (TokenType type, String lexeme) throws IllegalArgumentException {
        if (type.getPattern().matcher(lexeme).matches()) {
            this.type = type;
            this.lexeme = lexeme;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /** Obtener su categoria lexica */
    public TokenType getType () {
        return type;
    }

    /** Obtener su lexema */
    public String getLexeme () {
        return lexeme;
    }
}
