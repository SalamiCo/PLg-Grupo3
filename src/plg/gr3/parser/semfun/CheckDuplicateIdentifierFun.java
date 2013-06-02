package plg.gr3.parser.semfun;

import plg.gr3.errors.compile.DuplicateIdentifierError;
import plg.gr3.parser.Lexeme;
import plg.gr3.parser.Scope;
import plg.gr3.parser.SymbolTable;
import es.ucm.fdi.plg.evlib.Atributo;
import es.ucm.fdi.plg.evlib.SemFun;

public enum CheckDuplicateIdentifierFun implements SemFun {
    /** Única instancia de la clase */
    INSTANCE;

    @Override
    public Object eval (Atributo... args) {
        SymbolTable st = (SymbolTable) args[0].valor();
        Lexeme ident = (Lexeme) args[1].valor();
        Scope scope = (Scope) args[2].valor();

        if (st.hasIdentifier(ident.getLexeme()) && st.getIdentfierScope(ident.getLexeme()) == scope) {
            return new DuplicateIdentifierError(ident.getLexeme(), ident.getLine(), ident.getColumn());
        }

        return null;
    }
}
