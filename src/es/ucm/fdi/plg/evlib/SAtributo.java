package es.ucm.fdi.plg.evlib;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import plg.gr3.debug.Debugger;

public class SAtributo extends Atributo {
    private Atributo[] dependeDe;

    private SemFun semfun;

    private Object valor;

    private boolean calculado;

    public SAtributo () {
        this("");
    }

    public SAtributo (String doc) {
        super(doc);
        this.valor = null;
        this.calculado = false;
    }

    @Override
    public Object valor () {
        if (!calculado) {
            if (semfun == null) {
                Debugger.INSTANCE.error("Atributo sin definir!!");
                valor = null;
            } else {
                valor = semfun.eval(dependeDe);
            }
            debug(valor);
            calculado = true;
        }
        return valor;
    }

    public List<Atributo> dependencias () {
        if (dependeDe == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(Arrays.asList(dependeDe));
        }
    }

    public void fijaValor (Object valor) {
        this.valor = valor;
        calculado = true;
    }

    @Override
    public boolean calculado () {
        return calculado;
    }

    public void ponDependencias (Atributo... as) {
        dependeDe = as;
    }

    public void ponSemFun (SemFun semfun) {
        this.semfun = semfun;
    }
}
