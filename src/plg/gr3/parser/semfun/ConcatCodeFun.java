package plg.gr3.parser.semfun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import plg.gr3.vm.instr.Instruction;
import es.ucm.fdi.plg.evlib.Atributo;
import es.ucm.fdi.plg.evlib.SemFun;

public enum ConcatCodeFun implements SemFun {

    INSTANCE;

    @SuppressWarnings("unchecked")
    public List<Instruction> eval (Atributo... attrs) {
        List<Instruction> code = Collections.checkedList(new ArrayList<Instruction>(), Instruction.class);

        for (Atributo attr : attrs) {
            Object obj = (attr == null) ? null : attr.valor();
            if (obj instanceof Instruction) {
                code.add((Instruction) obj);

            } else if (obj instanceof List<?>) {
                code.addAll((List<Instruction>) obj);

            } else if (obj instanceof Instruction[]) {
                code.addAll(Arrays.asList((Instruction[]) obj));

            } else if (obj instanceof Atributo[]) {
                code.addAll(eval((Atributo[]) obj));
            }
        }

        return Collections.unmodifiableList(code);

    }
}
