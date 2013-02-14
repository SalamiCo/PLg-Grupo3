package plg.gr3.errors.compile;

/**
 * Error de identificador no definido
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class UndefinedIdentifierError extends CompileError {
    
    private String identName;
    
    /**
     * @param identName Nombre del identificador
     * @param line Línea en la que se dio
     * @param column Columna en la que se dio
     */
    public UndefinedIdentifierError (String identName, int line, int column) {
        super(line, column);
        this.identName = identName;
    }
    
    @Override
    public String getErrorMessage () {
        final String format = "El identificador %s no esta definido ";
        return String.format(format, identName);
    }
    
}
