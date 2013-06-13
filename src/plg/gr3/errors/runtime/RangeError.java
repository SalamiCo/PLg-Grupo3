package plg.gr3.errors.runtime;

import plg.gr3.data.Value;
import plg.gr3.vm.instr.Instruction;

public final class RangeError extends RuntimeError {

    private final Value value;

    private final int size;

    public RangeError (int position, Instruction instruction, Value value, int size) {
        super(position, instruction);

        this.value = value;
        this.size = size;
    }

    @Override
    public String getErrorMessage () {
        return String.format("Se ha intentado acceder con posición '%s' a un array de tamaño '%d'", value, size);
    }

}
