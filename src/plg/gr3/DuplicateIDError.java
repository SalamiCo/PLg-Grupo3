package plg.gr3;

import plg.gr3.errors.compile.CompileError;

//Clase que hereda de Compile error que maneja los errores de cuando se declara un mismo identificador dos veces

public class DuplicateIDError extends CompileError {
    
    String ident;
    
    public DuplicateIDError (String ident, int line, int column) {
        super(line, column);
        
        this.ident = ident;
    }
    
    @Override
    public String getErrorMessage () {
        final String format = "El identificador %s se ha declarado dos veces ";
        return String.format(format, ident);
    }
    
}
