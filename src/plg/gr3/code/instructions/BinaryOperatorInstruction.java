package plg.gr3.code.instructions;

import java.util.EmptyStackException;

import plg.gr3.BinaryOperator;
import plg.gr3.DivisionByZeroError;
import plg.gr3.EmptyStackError;
import plg.gr3.NegativeNaturalError;
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
     * @param operator
     *            Operador usado por esta instrucción
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
            //Operando2 = Cima
            Object op2 = vm.popValue();
            //Operando1 = Cima - 1
            Object op1 = vm.popValue();
            
            //obtener el operador que se va a aplicar (en función de swap1, swap2)
            BinaryOperator swappedOperator = vm.getSwappedOperator(operator);
            
            //Calcular el resultado
            Object res = swappedOperator.apply(op1, op2);
            //resultado en la Cima
            vm.pushValue(res);
            
        } catch (IllegalArgumentException e1) {
            //Si se restan 2 naturales y a < b, error en tiempo de ejecución
            vm.abort(new NegativeNaturalError(vm.getProgramCounter(), this));
            //TODO Añadir TypeMismatchError
        } catch (ArithmeticException e2) {
            //Division entre 0
            vm.abort(new DivisionByZeroError(vm.getProgramCounter(), this));
        } catch (EmptyStackException e3) {
            //Error de pila vacía
            vm.abort(new EmptyStackError(vm.getProgramCounter(), this));
        }
    }
}
