package plg.gr3.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import plg.gr3.code.instructions.Instruction;

/**
 * Lector de instrucciones abstracto.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public abstract class CodeReader {
    
    /** @return La siguiente instrucción de este lector */
    public abstract Instruction read ();
    
    /** @return La lista con todas las instrucciones restantes de este lector */
    public final List<Instruction> readAll () {
        List<Instruction> list = new ArrayList<>();
        
        Instruction inst;
        while ((inst = read()) != null) {
            list.add(inst);
        }
        
        return Collections.unmodifiableList(list);
    }
    
    /* package */CodeReader () {
        
    }
}
