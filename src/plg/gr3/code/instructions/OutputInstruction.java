package plg.gr3.code.instructions;

import java.io.IOException;

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
            vm.setOutput(vm.popValue()); //bufferOut = Cima
        } catch (IOException e) {
            //TODO error en tiempo de ejecución: No se puede mostrar [Expr.]
        }
        
    }
    
}
