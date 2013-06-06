package plg.gr3.vm.instr;

import java.util.Objects;

import plg.gr3.data.BooleanValue;
import plg.gr3.vm.VirtualMachine;

/**
 * Instruccióndesalto condicional
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class BranchInstruction extends Instruction {

    private final int address;

    private final BooleanValue value;

    /**
     * @param addr Dirección del salto
     * @param value Valor de la condición
     */
    public BranchInstruction (int addr, BooleanValue value) {
        this.address = addr;
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public void execute (VirtualMachine vm) {
        if (vm.popValue().equals(value)) {
            vm.setProgramCounter(address);
        }
    }

    /** @return Valor de la condición */
    public BooleanValue getConditionValue () {
        return value;
    }

    @Override
    public String toString () {
        return "BRANCH-IF-" + (value.getValue() ? "TRUE" : "FALSE") + "(0x" + Integer.toHexString(address) + ")";
    }

    /** @return Dirección del salto */
    public int getAddress () {
        return address;
    }
}
