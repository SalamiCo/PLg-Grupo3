package es.ucm.fdi.plg.evlib;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class TAtributos {
    private Map<String, SAtributo> atributos;

    public TAtributos () {
        atributos = new HashMap<>();
    }

    public void ponAtributo (String nombre, SAtributo a) {
        atributos.put(nombre, a);
    }

    public SAtributo a (String a) {
        if (!atributos.containsKey(a)) {
            throw new NoSuchElementException(a);
        }
        return atributos.get(a);
    }
}
