package plg.gr3.vm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import plg.gr3.code.Instruction;

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
        // Obtener la siguiente instrucción
        Instruction inst = program.get(programCounter);
        
        // Ejecutar la instrucción
        inst.execute(this);
        
        // Incrementar contador de programa
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
}
