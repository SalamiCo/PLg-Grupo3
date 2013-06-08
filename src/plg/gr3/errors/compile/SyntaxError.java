package plg.gr3.errors.compile;

import java_cup.runtime.Symbol;
import plg.gr3.Util;

public final class SyntaxError extends CompileError {

    private final Symbol symbol;

    private final String symName;

    public SyntaxError (Symbol symbol) {
        super(symbol.left, symbol.right);
        this.symbol = symbol;

        this.symName = Util.getSymbolName(symbol.sym);
    }

    @Override
    public String getErrorMessage () {
        return String.format(
            "Error de sintaxis en el símbolo '%s' de tipo '%s' (#%d)", symbol.value, symName, symbol.sym);
    }

}
