package plg.gr3.vm.instr;

import plg.gr3.data.IntegerValue;
import plg.gr3.data.NaturalValue;
import plg.gr3.data.Type;
import plg.gr3.data.Value;
import plg.gr3.errors.runtime.CastingError;
import plg.gr3.errors.runtime.RangeError;
import plg.gr3.vm.VirtualMachine;

public final class RangeCheckInstruction extends Instruction {

    private final int size;

    public RangeCheckInstruction (int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size: " + size + " < 0");
        }
        this.size = size;
    }

    @Override
    public void execute (VirtualMachine vm) {
        Value val = vm.peekValue();
        if (!((val instanceof IntegerValue) || (val instanceof NaturalValue))) {
            vm.abort(new CastingError(vm.getProgramCounter(), this, val, Type.INTEGER));
        }

        int i = val.toIntegerValue().getValue();
        if (i < 0 || i >= size) {
            vm.abort(new RangeError(vm.getProgramCounter(), this, val, size));
        }
    }

    @Override
    public String toString () {
        return "RANGE(" + size + ")";
    }

    public int getSize () {
        return size;
    }

}
