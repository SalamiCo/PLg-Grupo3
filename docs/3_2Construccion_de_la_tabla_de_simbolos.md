## 3.2.1 Funciones semanticas

creaTS() : TS
> Crea una tbala de símbolos vacía. 

añadeTipo(ts:TS, id:String, clase:String, dir:Int, tipo:CTipo)
>Añade a la tabla de símbolos el nuevo tipo construido, una variable o una constante. CTipo es el conjunto de propiedades con la información necesaria del tipo. Está explicado más adelante




## CTipo
CTipo es el conjunto de propiedades con la información necesaria del tipo. CTipo guarda información diferente dependiendo de si es un tipo construido, un array, una tupla, una variable de todo lo anterior dicho o bien una variable o constante de tipo básico. 

### CTipo en tipos construidos
Cuando la tabla de símbolos guarda un tipo construido, el campo tipo guarda la siguiente información

<t:reg, campos:CTipo, tam:int>

### CTipo en arrays
Cuando la tabla de símbolos guarda un array, el campo tipo guarda la siguiente información

<t:reg, campos:CTipo, tam:int>
