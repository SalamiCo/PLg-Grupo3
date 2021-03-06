package plg.gr3.vm.instr;

import java.io.IOException;
import java.util.EmptyStackException;

import plg.gr3.errors.runtime.EmptyStackError;
import plg.gr3.errors.runtime.IOError;
import plg.gr3.vm.VirtualMachine;

/**
 * Clase que implementa la instrucción <tt>out</tt>.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class OutputInstruction extends Instruction {
    
    @Override
    public void execute (VirtualMachine vm) {
        try {
            vm.writeValue(vm.popValue()); // bufferOut = Cima
        } catch (IOException e1) {
            vm.abort(new IOError(vm.getProgramCounter(), this));
        } catch (EmptyStackException e2) {
            vm.abort(new EmptyStackError(vm.getProgramCounter(), this));
        }
        
    }
    
    @Override
    public String toString () {
        return "OUT";
    }
}
