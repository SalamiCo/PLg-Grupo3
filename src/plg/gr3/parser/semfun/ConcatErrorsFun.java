package plg.gr3.parser.semfun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import plg.gr3.errors.compile.CompileError;
import es.ucm.fdi.plg.evlib.Atributo;
import es.ucm.fdi.plg.evlib.SemFun;

public enum ConcatErrorsFun implements SemFun {
    INSTANCE;

    @SuppressWarnings("unchecked")
    @Override
    public List<CompileError> eval (Atributo... attrs) {
        List<CompileError> errors = Collections.checkedList(new ArrayList<CompileError>(), CompileError.class);

        if (attrs == null) {
            return Collections.emptyList();
        }

        for (Atributo attr : attrs) {
            Object obj = (attr == null) ? null : attr.valor();
            if (obj instanceof CompileError) {
                errors.add((CompileError) obj);

            } else if (obj instanceof List<?>) {
                errors.addAll((List<CompileError>) obj);

            }
        }
        return Collections.unmodifiableList(errors);
    }
}
