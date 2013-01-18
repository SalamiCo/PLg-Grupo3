package plg.gr3.code.instructions;

import plg.gr3.UnaryOperator;
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
     * @param operator
     *            Operador de la instrucción
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
        //aplicar el operador al valor de la Cima
        Object result = operator.apply(vm.popValue());
        //poner el resultado en la Cima
        vm.pushValue(result);
    }
}
