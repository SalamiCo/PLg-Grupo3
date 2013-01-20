package plg.gr3.errors.compile;

/*
 * Clase que hereda de Compile error que maneja los errores en los que se usa un identificador que no ha sido
 * previamente declarado
 */

public class UndefinedIdentError extends CompileError {
    
    private String identName;
    
    public UndefinedIdentError (String identName, int line, int column) {
        super(line, column);
        this.identName = identName;
    }
    
    @Override
    public String getErrorMessage () {
        final String format = "El identificador %s no esta definido ";
        return String.format(format, identName);
    }
    
}
