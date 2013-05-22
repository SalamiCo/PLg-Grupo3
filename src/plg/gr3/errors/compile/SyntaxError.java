package plg.gr3.errors.compile;

import java_cup.runtime.Symbol;

public final class SyntaxError extends CompileError {
    
    private final Symbol symbol;
    
    public SyntaxError (Symbol symbol) {
        super(symbol.left, symbol.right);
        this.symbol = symbol;
    }
    
    @Override
    public String getErrorMessage () {
        return "Error de sintaxis en el símbolo '" + symbol.value + "' de tipo #" + symbol.sym;
    }
    
}
