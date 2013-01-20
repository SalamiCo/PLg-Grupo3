package plg.gr3.code.instructions;

import java.util.EmptyStackException;

import plg.gr3.BinaryOperator;
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
            
            //TODO si el operador es suma o resta mirar swap1
            //TODO si el operador en mult o div mirar swap2
            
            //Calcular el resultado
            Object res = operator.apply(op1, op2);
            //resultado en la Cima
            vm.pushValue(res);
            
        } catch (IllegalArgumentException e1) {
            //TODO Si se restan 2 naturales y a < b, error en tiempo de ejecución
        } catch (ArithmeticException e2) {
            //TODO Division entre 0
        } catch (EmptyStackException e3) {
            //TODO Error de pila vacía
        }
        //TODO Manejar excepciones como la de division por cero
    }
}
