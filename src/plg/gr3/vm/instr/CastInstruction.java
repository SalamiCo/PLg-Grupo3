package plg.gr3.vm.instr;

import plg.gr3.data.Type;
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
        //TODO crear un metodo para convertir los tipos "cast"
        // Type castingType = Type.forValue(vm.peekValue());
        //type del valor de la cima de la pila Type castedType = 
        //this.castType; 
        //if (Type.canCast(castingType, castedType)){
        //    cast(vm.peekValue(),castedType); 
        //}
        
    }
}
