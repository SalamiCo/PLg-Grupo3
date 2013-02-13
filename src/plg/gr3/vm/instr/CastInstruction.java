package plg.gr3.vm.instr;

import plg.gr3.data.Type;
import plg.gr3.data.Value;
import plg.gr3.errors.runtime.TypeMismatchError;
import plg.gr3.vm.VirtualMachine;

/**
 * Clase que implementa las instrucciones con operadores de casting.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class CastInstruction extends Instruction {
    
    /** Tipo al que hacer casting */
    private final Type castType;
    
    /**
     * @param castType Tipo al que hacer casting
     */
    public CastInstruction (Type castType) {
        this.castType = castType;
    }
    
    /** @return Tipo al qu hacer casting */
    public Type getCastType () {
        return castType;
    }
    
    @Override
    public void execute (VirtualMachine vm) {
        try {
            Value val = vm.popValue();
            Value casted = null;
            
            if (castType.equals(Type.CHARACTER)) {
                casted = val.toCharacterValue();
                
            } else if (castType.equals(Type.FLOAT)) {
                casted = val.toFloatValue();
                
            } else if (castType.equals(Type.INTEGER)) {
                casted = val.toIntegerValue();
                
            } else if (castType.equals(Type.NATURAL)) {
                casted = val.toNaturalValue();
                
            }
            
            vm.pushValue(casted);
            
        } catch (IllegalArgumentException exc) {
            vm.abort(new TypeMismatchError(vm.getProgramCounter(), this));
        }
    }
    
    @Override
    public String toString () {
        return "CAST(" + castType + ")";
    }
}
