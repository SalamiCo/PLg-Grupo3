package plg.gr3.vm.instr;

import plg.gr3.data.UnaryOperator;
import plg.gr3.data.Value;
import plg.gr3.errors.runtime.TypeMismatchError;
import plg.gr3.vm.VirtualMachine;

/**
 * Clase que engloba las operaciones unarias.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class UnaryOperatorInstruction extends Instruction {
    
    /** Operador a usar en la instrucción */
    private final UnaryOperator operator;
    
    /**
     * @param operator Operador de la instrucción
     */
    public UnaryOperatorInstruction (UnaryOperator operator) {
        this.operator = operator;
    }
    
    /** @return Operador usado por la instrucción */
    public UnaryOperator getOperator () {
        return operator;
    }
    
    @Override
    public void execute (VirtualMachine vm) {
        Value val = vm.popValue();
        try {
            Value result = operator.apply(val);
            vm.pushValue(result);
        } catch (IllegalArgumentException e) {
            // error, no se puede aplicar el operador (type mismatch)
            vm.abort(new TypeMismatchError(vm.getProgramCounter(), this, operator, val));
            
        }
    }
    
    @Override
    public String toString () {
        return "UN-OP[" + operator + "]";
    }
    
}
