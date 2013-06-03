package plg.gr3.errors.compile;

import plg.gr3.data.Type;

public final class InvalidTypeError extends CompileError {

    private final Type type;

    public InvalidTypeError (Type badType, int line, int column) {
        super(line, column);
        this.type = badType;
    }

    @Override
    public String getErrorMessage () {
        return "No se puede realizar esa operación en el tipo " + type;
    }

}
