package plg.gr3.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import plg.gr3.data.Operator;
import plg.gr3.data.Type;
import plg.gr3.data.Value;
import plg.gr3.errors.compile.CompileError;
import plg.gr3.vm.instr.Instruction;

/**
 * Clase que representa los atributos, heredados y sintetizados, de cualquiera de los nodos del arbol sintáctico.
 * <p>
 * Para instanciar esta clase, debe crearse primero una instancia de {@link Builder}, llamar a sus métodos para
 * establecer los valores, y finalmente llamar a {@link Builder#create() create()} para construir un nuevo objeto.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class Attributes {
    
    /** Atributos creados por defecto */
    public static final Attributes DEFAULT = new Attributes.Builder().create();
    
    /** Nombre del identificador */
    private final String ident;
    
    /** Tipo de una variable o expresión */
    private final Type type;
    
    /** Tipo de una variable o expresión */
    private final Type asigType;
    
    /** Si se trata de una constante o variable */
    private final boolean constant;
    
    /** Si hubo errores y cuales */
    private final Collection<CompileError> errors = new ArrayList<>();
    
    /** Instrucciones ya creadas pero sin generar */
    private final List<Instruction> instructions = new LinkedList<>();
    
    /** El operador a usar */
    private final Operator operator;
    
    /** El valor a usar */
    private final Value value;
    
    /** La dirección de memoria */
    private final int address;
    
    /**
     * @param ident Nombre del identificador
     * @param address La dirección de memoria
     * @param constant Si se trata de una constante o variable
     * @param errors Si hubo errores y cual
     * @param operator El operador a usar
     * @param type Tipo de una variable o expresión
     * @param asigType Tipo de la asignación
     * @param value El valor a usar
     * @param instructions Lista de instrucciones
     */
    private Attributes (
        String ident, int address, boolean constant, Collection<CompileError> errors, Operator operator, Type type,
        Type asigType, Value value, List<Instruction> instructions)
    {
        this.ident = ident;
        this.address = address;
        this.constant = constant;
        this.errors.addAll(errors);
        this.instructions.addAll(instructions);
        this.operator = operator;
        this.type = type;
        this.asigType = asigType;
        this.value = value;
    }
    
    /**
     * @return El atributo <tt>ident</tt>
     */
    public String getIdentifier () {
        return ident;
    }
    
    /**
     * @return El atributo <tt>type</tt>
     */
    public Type getType () {
        return type;
    }
    
    /**
     * @return El atributo <tt>constant</tt>
     */
    public boolean getConstant () {
        return constant;
    }
    
    /** @return El atributo <tt>error</tt> */
    public Collection<CompileError> getErrors () {
        return Collections.unmodifiableCollection(errors);
    }
    
    /** @return El atributo <tt>instructions</tt> */
    public List<Instruction> getInstructions () {
        return Collections.unmodifiableList(instructions);
    }
    
    /** @return El atributo <tt>operator</tt> */
    public Operator getOperator () {
        return getOperator(Operator.class);
    }
    
    /**
     * @param type Tipo de operador a usar
     * @return El atributo <tt>operator</tt>
     */
    public <T extends Operator> T getOperator (Class<T> type) {
        if (type.isInstance(operator)) {
            return type.cast(operator);
        }
        return null;
    }
    
    /** @return El atributo <tt>value</tt> */
    public Value getValue () {
        return getValue(Value.class);
    }
    
    /**
     * @param cls Tipo del atributo
     * @return El atributo <tt>value</tt> si es del tipo especificado, <tt>null</tt> en caso contrario
     */
    public <T extends Value> T getValue (Class<T> cls) {
        return cls.isInstance(value) ? cls.cast(value) : null;
    }
    
    /**
     * @return El atributo <tt>address</tt>
     */
    public int getAddress () {
        return address;
    }
    
    /** @return El tipo de la asignaión */
    public Type getAsigType () {
        return asigType;
    }
    
    /**
     * Clase constructora de objetos de atributos, creada para facilitarnos la vida en la mayoría de casos y eliminar
     * los constructores kilométricos del resto del código.
     * 
     * @author PLg Grupo 03 2012/2013
     */
    public static final class Builder {
        private String ident;
        
        private Type type;
        
        private Type asigType;
        
        private boolean constant;
        
        private final Collection<CompileError> errors = new ArrayList<>();
        
        private Operator operator;
        
        private Value value;
        
        private int address;
        
        private final List<Instruction> instructions = new LinkedList<>();
        
        /**
         * @param ident Identificador
         * @return <tt>this</tt>
         */
        public Builder identifier (String ident) {
            this.ident = ident;
            return this;
        }
        
        /**
         * @param address Dirección de memoria
         * @return <tt>this</tt>
         */
        public Builder address (int address) {
            this.address = address;
            return this;
        }
        
        /**
         * @param constant Si se trata de una constante
         * @return <tt>this</tt>
         */
        public Builder constant (boolean constant) {
            this.constant = constant;
            return this;
        }
        
        /**
         * @param errors Errores de compilación
         * @return <tt>this</tt>
         */
        public Builder errors (Collection<CompileError> errors) {
            this.errors.addAll(errors);
            return this;
        }
        
        /**
         * @param error Error de compilación
         * @return <tt>this</tt>
         */
        public Builder error (CompileError error) {
            this.errors.add(error);
            return this;
        }
        
        /**
         * @param operator Operador
         * @return <tt>this</tt>
         */
        public Builder operator (Operator operator) {
            this.operator = operator;
            return this;
        }
        
        /**
         * @param type Tipo
         * @return <tt>this</tt>
         */
        public Builder type (Type type) {
            this.type = type;
            return this;
        }
        
        /**
         * @param type Tipo de la asignación
         * @return <tt>this</tt>
         */
        public Builder asignationType (Type type) {
            this.asigType = type;
            return this;
        }
        
        /**
         * @param value Valor
         * @return <tt>this</tt>
         */
        public Builder value (Value value) {
            this.value = value;
            return this;
        }
        
        /**
         * @param instructions Instrucciones
         * @return <tt>this</tt>
         */
        public Builder instructions (Collection<? extends Instruction> instructions) {
            this.instructions.addAll(instructions);
            return this;
        }
        
        /**
         * @param instruction Instrucción
         * @return <tt>this</tt>
         */
        public Builder instruction (Instruction instruction) {
            this.instructions.add(instruction);
            return this;
        }
        
        /** @return El objeto {@link Attributes} construído */
        public Attributes create () {
            return new Attributes(ident, address, constant, errors, operator, type, asigType, value, instructions);
        }
    }
}
