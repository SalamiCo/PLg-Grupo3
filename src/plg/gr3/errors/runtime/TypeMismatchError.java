package plg.gr3.errors.runtime;

import plg.gr3.data.BinaryOperator;
import plg.gr3.data.Operator;
import plg.gr3.data.UnaryOperator;
import plg.gr3.data.Value;
import plg.gr3.vm.instr.Instruction;

/**
 * @author PLg Grupo 03 2012/2013
 */
public final class TypeMismatchError extends RuntimeError {
    
    private final Operator operator;
    
    private final Value valA;
    
    private final Value valB;
    
    /**
     * @param position Posición de instrucción
     * @param instruction Instrucción implicada
     * @param op Operador implicado
     * @param v1 Primer operando
     * @param v2 Segundo operando
     */
    public TypeMismatchError (int position, Instruction instruction, BinaryOperator op, Value v1, Value v2) {
        super(position, instruction);
        
        this.operator = op;
        this.valA = v1;
        this.valB = v2;
    }
    
    /**
     * @param position Posición de intrucción
     * @param instruction Instrución implicada
     * @param op Operador implicado
     * @param val Valor implicado
     */
    public TypeMismatchError (int position, Instruction instruction, UnaryOperator op, Value val) {
        super(position, instruction);
        
        this.operator = op;
        this.valA = val;
        this.valB = null;
    }
    
    @Override
    public String getErrorMessage () {
        if (operator instanceof BinaryOperator) {
            return String.format("No se pudo aplicar el operador %s a los operandos %s y %s", operator, valA, valB);
        } else {
            return String.format("No se pudo aplicar el operador %s al operando %s", operator, valA);
        }
    }
}
