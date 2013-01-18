package plg.gr3.code.instructions;

import plg.gr3.parser.Type;
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
     * @param castType
     *            Tipo al que hacer casting
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
        /*
         * Type castingType = Type.forValue(vm.peekValue()); //type del valor de la cima de la pila Type castedType =
         * this.castType; if (Type.canCast(castingType, castedType)){ cast(vm.peekValue(),castedType); }
         */
    }
}
