package plg.gr3.errors.runtime;

import plg.gr3.vm.instr.Instruction;

public final class TypeMismatchError extends RuntimeError {
    
    public TypeMismatchError (int position, Instruction instruction) {
        super(position, instruction);
    }
    
    @Override
    public String getErrorMessage () {
        //TODO Lanzar un mensaje con el tipo del operando y el operador
        //TODO Mostrar la línea donde se produjo el error y la instrucción
        return "Error de tipos" + this.getInstruction().toString();
    }
    
}
