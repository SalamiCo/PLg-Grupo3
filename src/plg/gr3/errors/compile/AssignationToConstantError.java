package plg.gr3.errors.compile;

public class AssignationToConstantError extends CompileError {
    
    private String constName;
    
    /*
     * Clase que hereda de Compile error que maneja los errores en los que se intenta asignar una constante
     */
    public AssignationToConstantError (String identName, int line, int column) {
        super(line, column);
        this.constName = identName;
    }
    
    @Override
    public String getErrorMessage () {
        final String format = "No se puede realizar una asignación a la constante '%s'";
        return String.format(format, constName);
    }
    
}
