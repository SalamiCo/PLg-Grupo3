package plg.gr3.parser.semfun;

import es.ucm.fdi.plg.evlib.Atributo;
import es.ucm.fdi.plg.evlib.SemFun;

public enum AssignationFun implements SemFun {
    INSTANCE;

    @Override
    public Object eval (Atributo... args) {
        return (args == null || args.length == 0 || args[0] == null) ? null : args[0].valor();
    }
}
