package plg.gr3.code;

/**
 * Clase que engloba las operaciones unarias. Hereda de Instruction.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class UnaryOpInstruction extends Instruction {
    
    /**
     * Operador unario de la instruccion de la maquina pila
     * */
    private UnaryOperator op;
    
    /**
     * Constructor de la instruccion con operador unario
     * 
     * @param op
     *            operador unario
     * */
    public InstructionUnaryop (UnaryOperator op) {
        /*
         * Se supone que el operador es unario en Instruction existe un metodo estatico forOperatorque crea una
         * InstructionUnaryOp si el operador es binario
         */
        super();
        this.op = op;
    }
}
