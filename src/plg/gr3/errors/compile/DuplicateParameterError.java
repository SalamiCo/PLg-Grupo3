package plg.gr3.errors.compile;

public final class DuplicateParameterError extends CompileError {

    private final String param;

    public DuplicateParameterError (String string, int line, int column) {
        super(line, column);
        this.param = string;
    }

    @Override
    public String getErrorMessage () {
        return String.format("El parámetro '%s' se ha definido varias veces", param);
    }

}
