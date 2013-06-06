package plg.gr3.errors.compile;

/**
 * Error que el numero de parámetros con el que se invoca la función es diferente de los declarados
 * 
 * @author PLg Grupo 03 2012/2013
 */

public class InvalidNumberOfParametersError extends CompileError {

    private int realParams;

    private int formalParams;

    /**
     * @param identName Nombre del identificador
     * @param line Línea en la que se dio
     * @param column Columna en la que se dio
     */
    public InvalidNumberOfParametersError (int realParams, int formalParams, int line, int column) {
        super(line, column);
        this.realParams = realParams;
        this.formalParams = formalParams;
    }

    @Override
    public String getErrorMessage () {
        final String format =
            "El número de parámetros con el que has invocado la función no coincide con el número de parámetros con el que está declarada. ";
        return String.format(format, realParams, formalParams);
    }

}
