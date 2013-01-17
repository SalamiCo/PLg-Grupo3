package plg.gr3;

import plg.gr3.parser.Type;

//Clase que hereda de Compile error que maneja los errores de tipos en operacion
//es decir, no pordemos hacer (nat+boolean;char mod float etc)

public class OperatorError extends CompileError {
    
    private Type typeA;
    
    private Type typeB;
    
    private Operator op;
    
    private boolean esOpBinario;
    
    //Constructor para cuando los operadores son binarios
    public OperatorError (Type typeA, Type typeB, Operator op, int line, int column) {
        super(line, column);
        this.typeA = typeA;
        this.typeB = typeB;
        this.op = op;
        esOpBinario = true;
        
    }
    
    //Constructor para cuando el operador el unario. (Solo entra un tipo)
    public OperatorError (Type typeA, Operator op, int line, int column) {
        super(line, column);
        this.typeA = typeA;
        this.op = op;
        esOpBinario = false;
        
    }
    
    @Override
    public String getErrorMessage () {
        final String format;
        if (esOpBinario) {
            format = "No puedes aplicar el operador binario " + op.toString() + "a los tipos %s y %s ";
            return String.format(format, typeA, typeB);
        } else {
            format = "No puedes aplicar el operador unario " + op.toString() + "al tipo %s";
            return String.format(format, typeA);
        }
        
    }
}
