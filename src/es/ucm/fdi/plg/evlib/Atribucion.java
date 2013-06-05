package es.ucm.fdi.plg.evlib;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import plg.gr3.debug.Debugger;

public class Atribucion {

    private final List<SAtributo> atributos = new LinkedList<>();

    private String regla;

    public void regla (String regla) {
        this.regla = regla;
    }

    public TAtributos atributosPara (String simbolo, String... as) {
        TAtributos tas = new TAtributos();
        for (String an : as) {
            tas.ponAtributo(an, new SAtributo(regla + "||" + simbolo + "." + an));
        }
        return tas;
    }

    public LAtributo atributoLexicoPara (String simbolo, String an, Object val) {
        return new LAtributo(regla + "||" + simbolo + "." + an, val);
    }

    public void dependencias (SAtributo a, Atributo... usados) {
        a.ponDependencias(usados);
        atributos.add(a);
    }

    public void calculo (SAtributo a, SemFun semfun) {
        a.ponSemFun(semfun);
    }

    public static void activaDebug () {
        Atributo.fijaDebug(true);
    }

    public final void calc () {
        int iteration = 0;
        int lastSize = -1;
        while (!atributos.isEmpty()) {
            if (atributos.size() == lastSize) {
                Debugger.INSTANCE.debug("Stall!! Calculating some attribute...");
                atributos.get(0).valor();
                atributos.remove(0);
            }

            iteration++;
            Debugger.INSTANCE.debug("Iteration %d: %d attributes left", iteration, atributos.size());
            lastSize = atributos.size();

            for (Iterator<SAtributo> it = atributos.iterator(); it.hasNext();) {
                SAtributo attr = it.next();

                List<Atributo> deps = attr.dependencias();
                boolean calculable = true;
                for (Atributo dep : deps) {
                    if (!dep.calculado()) {
                        calculable = false;
                    }
                }

                if (calculable) {
                    attr.valor();
                    it.remove();
                }
            }
        }

        Debugger.INSTANCE.debug("Finished iterating!!");
    }
}
