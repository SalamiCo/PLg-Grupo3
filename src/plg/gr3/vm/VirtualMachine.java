package plg.gr3.vm;

import java.util.ArrayList;
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
        this.program = program;
        
        memory = new ArrayList<>();
        stack = new Stack<>();
        programCounter = 0;
        stopped = false;
        swapped1 = false;
        swapped2 = false;
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
    
    public void changeStop () {
        if (stopped) {
            stopped = false;
        } else {
            stopped = true;
        }
    }
    
    public void changeSwapped1 () {
        if (swapped1) {
            swapped1 = false;
        } else {
            swapped1 = true;
        }
    }
    
    public void changeSwapped2 () {
        if (swapped2) {
            swapped2 = false;
        } else {
            swapped2 = true;
        }
    }
    
}
