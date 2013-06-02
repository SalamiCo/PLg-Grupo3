package plg.gr3.parser.semfun;

import es.ucm.fdi.plg.evlib.Atributo;
import es.ucm.fdi.plg.evlib.SemFun;

public final class IncrementFun implements SemFun {

    private final int increment;

    public IncrementFun (int increment) {
        this.increment = increment;
    }

    @Override
    public Object eval (Atributo... args) {
        Integer value = (Integer) args[0].valor();
        return value + increment;
    }

}
