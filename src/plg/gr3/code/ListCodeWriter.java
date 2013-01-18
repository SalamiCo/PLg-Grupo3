package plg.gr3.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import plg.gr3.code.instructions.Instruction;

public final class ListCodeWriter extends CodeWriter {
    
    private final List<Instruction> list;
    
    private boolean inhibited = false;
    
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
    }
    
    public List<Instruction> getList () {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }
}
