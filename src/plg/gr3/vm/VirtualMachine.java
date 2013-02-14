package plg.gr3.vm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import plg.gr3.data.BinaryOperator;
import plg.gr3.data.BooleanValue;
import plg.gr3.data.CharacterValue;
import plg.gr3.data.FloatValue;
import plg.gr3.data.IntegerValue;
import plg.gr3.data.NaturalValue;
import plg.gr3.data.Type;
import plg.gr3.data.Value;
import plg.gr3.errors.runtime.EmptyStackError;
import plg.gr3.errors.runtime.InvalidInstructionAddressError;
import plg.gr3.errors.runtime.RuntimeError;
import plg.gr3.vm.instr.Instruction;

/**
 * Máquina virtual del lenguaje
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class VirtualMachine {
    
    private volatile List<Value> memory;
    
    private volatile List<Instruction> program;
    
    private volatile Stack<Value> stack;
    
    private volatile int programCounter;
    
    private volatile boolean stopped;
    
    private volatile boolean swapped1;
    
    private volatile boolean swapped2;
    
    private volatile BufferedReader reader;
    
    private volatile Writer writer;
    
    private volatile RuntimeError error;
    
    public VirtualMachine (List<Instruction> program) {
        this.program = Collections.unmodifiableList(program);
        
        memory = Collections.synchronizedList(new ArrayList<Value>());
        stack = new Stack<>();
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = new OutputStreamWriter(System.out);
        reset();
    }
    
    public void setReader (Reader reader) {
        this.reader = new BufferedReader(reader);
    }
    
    public void setWriter (Writer writer) {
        this.writer = writer;
    }
    
    public void execute () {
        while (!stopped) {
            step();
        }
    }
    
    public void step () {
        Instruction inst = getInstruction(programCounter);
        try {
            if (inst != null) {
                inst.execute(this);
                programCounter++;
            }
        } catch (EmptyStackException e) {
            // Error de pila vacía
            this.abort(new EmptyStackError(programCounter, inst));
        }
    }
    
    public Instruction getInstruction (int address) {
        if (address < 0 || address >= program.size()) {
            abort(new InvalidInstructionAddressError(address));
            return null;
        }
        return program.get(address);
    }
    
    public Value getMemoryValue (int address) {
        if (address < 0 || address >= memory.size()) {
            return null;
        }
        return memory.get(address);
        
    }
    
    public void setMemoryValue (int address, Value value) {
        while (memory.size() <= address) {
            memory.add(null);
        }
        
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
    
    public Value readValue (Type type) throws IOException {
        String str = reader.readLine();
        if (type.equals(Type.NATURAL)) {
            return NaturalValue.valueOf(str);
            
        } else if (type.equals(Type.INTEGER)) {
            return IntegerValue.valueOf(str);
            
        } else if (type.equals(Type.FLOAT)) {
            return FloatValue.valueOf(str);
            
        } else if (type.equals(Type.CHARACTER)) {
            return CharacterValue.valueOf(str);
            
        } else if (type.equals(Type.BOOLEAN)) {
            return BooleanValue.valueOf(str);
        }
        
        throw new IllegalArgumentException(type.toString());
    }
    
    public void writeValue (Value value) throws IOException {
        writer.write(value.toString());
        writer.flush();
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
    
    public ArrayList<String> getStackToArrayList () {
        ArrayList<String> stackArrayList = new ArrayList<String>();
        
        for (Value value : stack) {
            stackArrayList.add(value.toString());
        }
        
        return stackArrayList;
    }
}
