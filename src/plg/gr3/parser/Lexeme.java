package plg.gr3.parser;

import java.util.Objects;

public final class Lexeme {
    private final String lexeme;

    private final int line;

    private final int column;

    public Lexeme (String lexeme, int line, int column) {
        if (line < 0) {
            throw new IllegalArgumentException("line: " + line + " < 0");
        }
        if (column < 0) {
            throw new IllegalArgumentException("column: " + column + " < 0");
        }
        this.lexeme = Objects.requireNonNull(lexeme, "lexeme");
        this.line = line;
        this.column = column;
    }

    public String getLexeme () {
        return lexeme;
    }

    public int getLine () {
        return line;
    }

    public int getColumn () {
        return column;
    }

    @Override
    public String toString () {
        return "\"" + lexeme + "\"@" + line + "," + column;
    }
}
