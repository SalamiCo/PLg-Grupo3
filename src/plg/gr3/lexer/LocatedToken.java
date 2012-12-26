package plg.gr3.lexer;

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
     * @param token
     *            Token al que se hace referencia. Debe ser distinto de null.
     * @param line
     *            Linea donde se ubica el token en el fichero. Debe ser mayor que 0.
     * @param column
     *            Columna donde se ubica el token en el fichero. Debe ser mayor que 0.
     * @throws IllegalArgumentException
     *             Si alguna de las precondiciones no se cumple.
     * 
     * */
    public LocatedToken (Token token, int line, int column) throws IllegalArgumentException {
        if (token != null && line > 0 && column > 0) {
            this.token = token;
            this.line = line;
            this.column = column;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**@return Token al que se hace referencia */
    public Token getToken () {
        return token;
    }

    /**@return Linea donde se ubica el token en el fichero */
    public int getLine () {
        return line;
    }

    /**@return Columna donde se ubica el token en el fichero */
    public int getColumn () {
        return column;
    }

}
