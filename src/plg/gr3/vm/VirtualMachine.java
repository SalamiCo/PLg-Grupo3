package plg.gr3.vm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import plg.gr3.code.instructions.Instruction;

public final class VirtualMachine {
    
    private List<Object> memory;
    
    private List<Instruction> program;
    
    private Stack<Object> stack;
    
    private int programCounter;
    
    private boolean stopped;
    
    private boolean swapped1;
    
    private boolean swapped2;
    
    public VirtualMachine (List<Instruction> program) {
        this.program = Collections.unmodifiableList(program);
        
        memory = new ArrayList<>();
        stack = new Stack<>();
        
        reset();
    }
    
    public void execute () {
        while (!stopped) {
            step();
        }
    }
    
    public void step () {
        // Ejecuta la siguiente instrucción e incrementa el contador de programa
        Instruction inst = getInstruction(programCounter);
        inst.execute(this);
        programCounter++;
    }
    
    public Instruction getInstruction (int position) {
        return program.get(position);
    }
    
    public Object getMemoryValue (int position) {
        return memory.get(position);
    }
    
    public void setMemoryValue (int position, Object value) {
        memory.set(position, value);
    }
    
    public void stop () {
        stopped = true;
    }
    
    public void reset () {
        stopped = false;
        programCounter = 0;
        stopped = false;
        swapped1 = false;
        swapped2 = false;
        memory.clear();
    }
    
    public void toggleSwapped1 () {
        swapped1 = !swapped1;
    }
    
    public void toggleSwapped2 () {
        swapped2 = !swapped2;
    }
    
    public void pushValue (Object value) {
        stack.push(value);
    }
    
    public Object popValue () {
        return stack.pop();
    }
    
    public int getProgramCounter () {
        return programCounter;
    }
    
    public boolean isStopped () {
        return stopped;
    }
    
    public boolean isSwapped1 () {
        return swapped1;
    }
    
    public boolean isSwapped2 () {
        return swapped2;
    }
}
