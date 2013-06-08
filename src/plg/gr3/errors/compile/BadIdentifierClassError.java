package plg.gr3.errors.compile;

import plg.gr3.parser.ClassDec;

public class BadIdentifierClassError extends CompileError {

    String ident;

    String classDecName;

    String expectedClassDecName;

    public BadIdentifierClassError (String ident, ClassDec cd, ClassDec expected, int line, int column) {
        super(line, column);
        this.ident = ident;
        this.classDecName = cd.name();
        this.expectedClassDecName = expected.name();
    }

    @Override
    public String getErrorMessage () {
        final String format = "Se esperaba un identificador '%s' pero se encontró '%s' .";
        return String.format(format, ident, expectedClassDecName, classDecName);
    }

}
