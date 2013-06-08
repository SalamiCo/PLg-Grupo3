package plg.gr3.errors.compile;

import plg.gr3.parser.Lexeme;

/**
 * Error que el numero de parámetros con el que se invoca la función es diferente de los declarados
 * 
 * @author PLg Grupo 03 2012/2013
 */

public class InvalidNumberOfParametersError extends CompileError {

    private final int realParams;

    private final int formalParams;

    private final String ident;

    /**
     * @param identName Nombre del identificador
     * @param line Línea en la que se dio
     * @param column Columna en la que se dio
     */
    public InvalidNumberOfParametersError (Lexeme ident, int realParams, int formalParams) {
        super(ident.getLine(), ident.getColumn());
        this.ident = ident.getLexeme();
        this.realParams = realParams;
        this.formalParams = formalParams;
    }

    @Override
    public String getErrorMessage () {
        final String format = "La función '%s' llamada con %d argumentos está declarada con %d parámetros";
        return String.format(format, ident, realParams, formalParams);
    }
}
