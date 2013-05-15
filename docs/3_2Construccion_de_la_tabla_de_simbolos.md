## 3.2.1 Funciones semanticas

creaTS() : TS
> Crea una tbala de símbolos vacía. 

añade(ts:TS, id:String, clase:String, dir:Int, tipo:CTipo)
>Añade a la tabla de símbolos el nuevo tipo construido, una variable o una constante. CTipo es el conjunto de propiedades con la información necesaria del tipo. Está explicado más adelante

campo?(ts:TS, campos:CCampo, id:String)
>Devuelve true cuando la lista de campos de Campo contenga campo id. 


## CTipo
CTipo es el conjunto de propiedades con la información necesaria del tipo. CTipo guarda información diferente dependiendo de si es un tipo construido, un array, una tupla, una variable de todo lo anterior dicho o bien una variable o constante de tipo básico. 

### CTipo en tipos construidos
Cuando la tabla de símbolos guarda un tipo construido, el campo tipo guarda la siguiente información

    <t:reg, campos:CCampo, tam:int>

Donde `CCampo` es una lista de elementos de la forma

    <id:String, tipo:Ctipo>



### CTipo en arrays
Cuando la tabla de símbolos guarda un array, el campo tipo guarda la siguiente información. 

    <t:array, nelems:int, tbase:Ctipo, tam:int>

### Ctipo en tuplas
Cuando la tabla de símbolos guarda un array el campotipo guarda la siguiente información.  

    <t:tupla, nelems:int, campos:CCampos, tam:int>

Donde `campos` es una lista de elementos de la forma

    <id:int, tipo:CTipo>

### Ctipo en variables cuando guardan una referencia a otro tipo
Cuando la tabla de símbolos guarda una variable, con una referencia a otro tipo, el campo tipo guarda la siguiente información. 

    <t:ref, id:String, tam:int>

### Ctipo en constantes y variables que guardan un tipo primitivo
Cuando la tabla de símbolos guarda una constante o, una variable con tipos primitivos, el campo tipo guarda la siguiente información. 

    <t:int, tam:1> 
    <t:nat, tam:1>
    <t:float, tam:1>
    <t:bool, tam:1>
    <t:char, tam:1>
