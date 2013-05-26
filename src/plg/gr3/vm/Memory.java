package plg.gr3.vm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Memoria de la máquina virtual.
 * <p>
 * La memoria usa un sistema de "paginado" para permitir almacenar variables en cualquier dirección de memoria sin
 * gastar demasiada RAM.
 * 
 * @author PLg Grupo 03 2012/2013
 * @param <T> Tipo del contenido
 */
public final class Memory<T> {
    
    /** Tamaño de página */
    private static final int PAGE_SIZE = 1 << 8;
    
    /** Si la memoria se puede escribir */
    private boolean writable;
    
    /** Páginas de la memoria */
    private final Map<Integer, List<T>> pages;
    
    private Memory () {
        writable = true;
        pages = new HashMap<Integer, List<T>>();
    }
    
    /**
     * Devuelve el contenido de la posición <tt>addr</tt>, o <tt>null</tt> si no está definido.
     * 
     * @param addr Dirección de memoria
     * @return Valor de la dirección especificada
     */
    public T get (int addr) {
        if (addr < 0) {
            throw new IllegalArgumentException("addr < 0 (" + addr + ")");
        }
        
        int pageNum = addr / PAGE_SIZE;
        int offset = addr % PAGE_SIZE;
        
        List<T> page = pages.get(pageNum);
        if (page == null) {
            return null;
        }
        return page.get(offset);
    }
    
    /**
     * Establece el contenido de la posición <tt>addr</tt> a <tt>val</tt>.
     * 
     * @param addr Dirección de memoria
     * @param val Nuevo valor
     */
    public void set (int addr, T val) {
        if (addr < 0) {
            throw new IllegalArgumentException("addr < 0 (" + addr + ")");
        }
        if (!writable) {
            throw new IllegalStateException("Memory is not writable");
        }
        
        int pageNum = addr / PAGE_SIZE;
        int offset = addr % PAGE_SIZE;
        
        if (!pages.containsKey(Integer.valueOf(pageNum))) {
            @SuppressWarnings("unchecked")
            T[] arr = (T[]) new Object[PAGE_SIZE];
            pages.put(Integer.valueOf(pageNum), Arrays.asList(arr));
        }
        
        pages.get(Integer.valueOf(pageNum)).set(offset, val);
    }
    
    /** Borra completamente la memoria */
    public void clear () {
        pages.clear();
    }
    
    /**
     * Crea una memoria de sólo lectura con los contenidos iniciales al comienzo de la misma
     * 
     * @param contents Contenido inicial de la memoria.
     * @return Una memoria de sólo lectura
     */
    public static <T> Memory<T> readable (List<T> contents) {
        Memory<T> mem = new Memory<T>();
        for (int i = 0; i < contents.size(); i++) {
            mem.set(i, contents.get(i));
        }
        mem.writable = false;
        return mem;
    }
    
    /**
     * Crea una memoria vacía con posibilidad de escritura
     * 
     * @param type Tipo del contenido de la memoria
     * @return Una memoria con posibilidad de escritura
     */
    public static <T> Memory<T> writable (Class<T> type) {
        return new Memory<T>();
    }
}
