package plg.gr3.vm.instr;

import plg.gr3.vm.VirtualMachine;

public final class CommentedInstruction extends Instruction {

    private final Instruction instruction;

    private final String comment;

    public CommentedInstruction (Instruction instruction, String comment) {
        this.instruction = instruction;
        this.comment = comment;
    }

    @Override
    public void execute (VirtualMachine vm) {
        instruction.execute(vm);
    }

    @Override
    public String toString () {
        return instruction.toString();
    }

    public Instruction getOriginalInstruction () {
        return instruction;
    }

    public String getComment () {
        return comment;
    }

}
