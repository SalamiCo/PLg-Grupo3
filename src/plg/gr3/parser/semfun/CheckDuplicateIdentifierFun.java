package plg.gr3.parser.semfun;

import plg.gr3.errors.compile.DuplicateIdentifierError;
import plg.gr3.parser.SymbolTable;
import es.ucm.fdi.plg.evlib.Atributo;
import es.ucm.fdi.plg.evlib.SemFun;

public enum CheckDuplicateIdentifierFun implements SemFun {
    INSTANCE;

    @Override
    public Object eval (Atributo... args) {
        SymbolTable st = (SymbolTable) args[0].valor();
        String ident = (String) args[1].valor();

        if (st.hasIdentifier(ident)) {
            return new DuplicateIdentifierError(ident, -1, -1);
        } else {
            return null;
        }
    }
}
