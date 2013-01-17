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
    
    private boolean stopper;
    
    private boolean swapper1;
    
    private boolean swapper2;
    
    public VirtualMachine (List<Instruction> program) {
        this.program = program;
        
        memory = new ArrayList<>();
        stack = new Stack<>();
        programCounter = 0;
        stopper = false;
        swapper1 = false;
        swapper2 = false;
    }
    
}
