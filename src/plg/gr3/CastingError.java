package plg.gr3;

import plg.gr3.parser.Type;

//Clase que hereda de Compile error que maneja los errores de discordancia de tipos cuando hacemos el casting
public class CastingError extends CompileError {
    
    private Type typeCasting;
    
    private Type typeCasted;
    
    public CastingError (Type typeCasting, Type typeCasted, int line, int column) {
        super(line, column);
        
        this.typeCasting = typeCasting;
        this.typeCasted = typeCasted;
        
    }
    
    @Override
    public String getErrorMessage () {
        final String format = "No se puede hacer el casting del tipo %s al tipo %s ";
        return String.format(format, typeCasted, typeCasting);
    }
    
}
