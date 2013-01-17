package plg.gr3.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import plg.gr3.code.Instruction;

public class VirtualMachine {
    
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
        
    }
    
    public void step () {
        
    }
}
