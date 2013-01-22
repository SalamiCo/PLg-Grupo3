package plg.gr3.code;

import java.util.List;

import plg.gr3.vm.instr.Instruction;

/**
 * Escritor de código.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public abstract class CodeWriter {
    
    /* package */CodeWriter () {
        // Construtor de paquete para evitar herencia externa
    }
    
    /**
     * Escribe una instrucción en el destino de este escritor. Si la escritura de código se hainhibido, este método no
     * hace nada.
     * 
     * @param inst
     *            Instrucción a escribir
     */
    public abstract void write (Instruction inst);
    
    /**
     * Escribe todas las instrucciones de la lista pasada, en orden, en el destino de este escritor. Si la escritura de
     * código se ha inhibido, este método no hace nada.
     * 
     * @param insts
     *            Instrucciones a escribir
     */
    public final void write (List<Instruction> insts) {
        if (!isInhibited()) {
            for (Instruction inst : insts) {
                write(inst);
            }
        }
    }
    
    /**
     * Inhibe la escritura de código, impidiendo que se escriban nuevas instrucciones.
     */
    public abstract void inhibit ();
    
    /** @return <tt>true</tt> si la escritura de código se ha inhibido, <tt>false</tt> en otro caso */
    public abstract boolean isInhibited ();
}
