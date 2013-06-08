package plg.gr3.errors.compile;

/**
 * Error en el que se esperaba un designador pero se encontró otra cosa
 * 
 * @author PLg Grupo 03 2012/2013
 */

public class ExpectedDesignator extends CompileError {

    private String identName;

    public ExpectedDesignator (String ident, int line, int column) {
        super(line, column);
        this.identName = ident;
    }

    @Override
    public String getErrorMessage () {
        final String format = "Se esperaba que '%s' fuese un designador";
        return String.format(format, identName);
    }

}
