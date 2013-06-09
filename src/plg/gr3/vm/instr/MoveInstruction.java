package plg.gr3.vm.instr;

import plg.gr3.vm.VirtualMachine;

/**
 * Instrucción de mover en bloque de memoria.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class MoveInstruction extends Instruction {

    /** Tamaño de memoria a copiar */
    private final int size;

    /** @param size Tamaño de memoria a copiar */
    public MoveInstruction (int size) {
        this.size = size;
    }

    @Override
    public void execute (VirtualMachine vm) {
        int to = vm.popValue().toIntegerValue().getValue();
        int from = vm.popValue().toIntegerValue().getValue();

        for (int i = 0; i < size; i++) {
            vm.setMemoryValue(to + i, vm.getMemoryValue(from + i));
        }
    }

    /** @return Tamaño a mover por la instrucción */
    public int getSize () {
        return size;
    }

    @Override
    public String toString () {
        return "MOVE(" + size + ")";
    }
}
