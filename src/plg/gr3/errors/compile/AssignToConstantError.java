package plg.gr3.errors.compile;

public class AssignToConstantError extends CompileError {
    
    private String constName;
    
    /*
     * Clase que hereda de Compile error que maneja los errores en los que se intenta asignar una constante
     */
    public AssignToConstantError (String identName, int line, int column) {
        super(line, column);
        this.constName = identName;
    }
    
    @Override
    public String getErrorMessage () {
        final String format = "El identificador %s  es una constante no puedes modificar su valor ";
        return String.format(format, constName);
    }
    
}
