package plg.gr3.parser.semfun;

import es.ucm.fdi.plg.evlib.Atributo;
import es.ucm.fdi.plg.evlib.SemFun;

public enum AndFun implements SemFun {
    INSTANCE;

    @Override
    public Object eval (Atributo... args) {
        for (Atributo atr : args) {
            boolean b = ((Boolean) atr.valor()).booleanValue();
            if (!b) {
                return false;
            }
        }
        return true;
    }
}
