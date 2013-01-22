package plg.gr3.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import plg.gr3.vm.instr.Instruction;

/**
 * Escritor de código que generará directamente una lista de instrucciones.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class ListCodeWriter extends CodeWriter {
    
    /** Lista de instrucciones */
    private final List<Instruction> list;
    
    /** Si la escritura está inhibida */
    private boolean inhibited = false;
    
    /** Constructor por defecto */
    public ListCodeWriter () {
        list = new ArrayList<>();
    }
    
    @Override
    public void write (Instruction inst) {
        if (!inhibited) {
            list.add(inst);
        }
    }
    
    @Override
    public void inhibit () {
        inhibited = true;
        list.clear();
    }
    
    /** @return Devuelve una copia de la lista de instrucciones generada */
    public List<Instruction> getList () {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }
    
    @Override
    public boolean isInhibited () {
        return inhibited;
    }
}
