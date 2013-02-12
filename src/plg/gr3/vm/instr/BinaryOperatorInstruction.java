package plg.gr3.vm.instr;

import java.util.EmptyStackException;

import plg.gr3.data.BinaryOperator;
import plg.gr3.data.Value;
import plg.gr3.errors.runtime.DivisionByZeroError;
import plg.gr3.errors.runtime.EmptyStackError;
import plg.gr3.errors.runtime.NegativeNaturalError;
import plg.gr3.vm.VirtualMachine;

/**
 * Clase que engloba todas las operaciones binarias.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class BinaryOperatorInstruction extends Instruction {
    
    /** Operador binario de la instruccion de la maquina pila */
    private BinaryOperator operator;
    
    /**
     * @param operator Operador usado por esta instrucción
     */
    public BinaryOperatorInstruction (BinaryOperator operator) {
        this.operator = operator;
    }
    
    /** @return Operador usado por la instrucción */
    public BinaryOperator getOperator () {
        return operator;
    }
    
    @Override
    public void execute (VirtualMachine vm) {
        try {
            Value op2 = vm.popValue();
            Value op1 = vm.popValue();
            BinaryOperator swappedOperator = vm.getSwappedOperator(operator);
            
            Value res = swappedOperator.apply(op1, op2);
            
            vm.pushValue(res);
            
        } catch (IllegalArgumentException e1) {
            // Si se restan 2 naturales y a < b, error en tiempo de ejecución
            vm.abort(new NegativeNaturalError(vm.getProgramCounter(), this));
            // TODO Añadir TypeMismatchError
            
        } catch (ArithmeticException e2) {
            vm.abort(new DivisionByZeroError(vm.getProgramCounter(), this));
            
        } catch (EmptyStackException e3) {
            vm.abort(new EmptyStackError(vm.getProgramCounter(), this));
        }
    }
    
    @Override
    public String toString () {
        return "BIN-OP[" + operator + "]";
    }
}
