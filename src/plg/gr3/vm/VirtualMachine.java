package plg.gr3.vm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import plg.gr3.data.BinaryOperator;
import plg.gr3.data.Value;
import plg.gr3.errors.runtime.RuntimeError;
import plg.gr3.vm.instr.Instruction;

public final class VirtualMachine {
    
    private List<Value> memory;
    
    private List<Instruction> program;
    
    private Stack<Value> stack;
    
    private int programCounter;
    
    private boolean stopped;
    
    private boolean swapped1;
    
    private boolean swapped2;
    
    private BufferedReader reader;
    
    private BufferedWriter writer;
    
    private RuntimeError error;
    
    public VirtualMachine (List<Instruction> program) {
        this.program = Collections.unmodifiableList(program);
        
        memory = new ArrayList<>();
        stack = new Stack<>();
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = new BufferedWriter(new OutputStreamWriter(System.out));
        reset();
    }
    
    public void execute () {
        while (!stopped) {
            step();
        }
    }
    
    public void step () {
        Instruction inst = getInstruction(programCounter);
        inst.execute(this);
        programCounter++;
    }
    
    public Instruction getInstruction (int address) {
        return program.get(address);
    }
    
    public Value getMemoryValue (int address) {
        if (address >= 0 && address < memory.size()) {
            memory.get(address);
        }
        return null;
    }
    
    public void setMemoryValue (int address, Value value) {
        memory.set(address, value);
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
    
    public void abort (RuntimeError error) {
        this.error = Objects.requireNonNull(error, "error");
        error.print();
        
        stop();
    }
    
    public void toggleSwapped1 () {
        swapped1 = !swapped1;
    }
    
    public void toggleSwapped2 () {
        swapped2 = !swapped2;
    }
    
    public void pushValue (Value value) {
        stack.push(value);
    }
    
    public Value popValue () {
        return stack.pop();
    }
    
    public Value peekValue () {
        return stack.peek();
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
    
    // FIXME OMGWTFBBQ QUÉ COJONES
    public Object getInput () throws IOException {
        return reader.readLine();
    }
    
    public void setOutput (Object value) throws IOException {
        writer.write(value.toString());
    }
    
    public RuntimeError getError () {
        return error;
    }
    
    public BinaryOperator getSwappedOperator (BinaryOperator operator) {
        if (swapped1 && operator == BinaryOperator.ADDITION) {
            return BinaryOperator.SUBTRACTION;
            
        } else if (swapped1 && operator == BinaryOperator.SUBTRACTION) {
            return BinaryOperator.ADDITION;
            
        } else if (swapped2 && operator == BinaryOperator.PRODUCT) {
            return BinaryOperator.DIVISION;
            
        } else if (swapped2 && operator == BinaryOperator.DIVISION) {
            return BinaryOperator.PRODUCT;
            
        } else {
            return operator;
        }
    }
}
