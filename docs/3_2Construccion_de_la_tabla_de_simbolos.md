## 3.2.1 Funciones semanticas

creaTS() : TS
> Crea una tabla de símbolos vacía. 

añade(ts:TS, id:String, clase:String, nivel:String, dir:Int, tipo:CTipo) : TS
>Añade a la tabla de símbolos el nuevo tipo construido, una variable o una constante. CTipo es el conjunto de propiedades con la información necesaria del tipo. Está explicado más adelante

campo?(ts:TS, campos:CCampo, id:String) : Boolean
>Devuelve true cuando la lista de campos de Campo contenga campo id. 

desplazamiento(tipo:CTipo, id:String) : Integer
>Devuelve el tamaño que ocupa en memoria el identificador id


existeID(ts:TS, id:String) : Boolean
>Dada una tabla de símbolos y el campo id de un identificador, indica si el identificador existe en la tabla de símbolos (sensible a mayúsculas y minúsculas), es decir, ha sido previamente declarado.

obtieneCtipo(typeDesc:TypeDesc) : CTipo
>Dado un descriptor de tipos devuelve el CTipo asociado


## CTipo
CTipo es el conjunto de propiedades con la información necesaria del tipo. CTipo guarda información diferente dependiendo de si es un tipo construido, un array, una tupla, una variable de todo lo anterior dicho o bien una variable o constante de tipo básico. 

### CTipo en tipos construidos
Cuando la tabla de símbolos guarda un tipo construido, el campo tipo guarda la siguiente información

    <t:reg, tipo:Ctipo, tam:int>

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

### Ctipo en subprogramas
TODO


## 3.2.2 Atributos semánticos

**ts:** tabla de símbolos sintetizada

**id:** nombre del identificador

**dir:** Dirección de memoria. Dónde se guarda la variable o la constante

**nivel** Indica si el identificador es de ámbito global o local

**tipo** Almacena los conjuntos de propiedades con la información necesaria del tipo

**clase:** Indica si es la declaración de un tipo construido, un variable o una constante 

## 3.2.3 Gramáticas de atributos

A continuación se detalla la construcción de los atributos relevantes para la creación de la tabla de símbolos. Otros atributos, como la tabla de símbolos heredada (que tan solo se propaga) o el tipo y el valor de las expresiones se detallarán más adelante en sus correspondientes secciones.

    Program → program ident illave SConsts STypes SVars SSubprogs SInsts fllave fin
        Program.tsh = creaTS()
        Program.dirh = 0
        SConsts.tsh = Program.ts
        SConsts.dirh = Program.dir
        STypes.tsh = SConsts.ts
        STypes.dirh = SConsts.dir
        SVars.tsh = STypes.ts
        SVars.dirh = STypes.dir
        SSubprogs.tsh = SVars.ts
        SSubprogs.dirh = SVars.dir
        Program.ts ##???##

    SConsts → const illave Consts fllave 
        Consts.tsh = SConsts.tsh
        Consts.dirh = SConsts.dirh
        SConsts.ts = Consts.ts
        SConsts.dir = Consts.dir

    SConsts → ɛ
        SConsts.ts = SConsts.tsh
        SConsts.dir = SConsts.dirh

    Consts → Consts pyc Const
        Consts0.tsh = Consts1.tsh
        Consts0.dirh = Consts1.dirh
        Const.ts = Consts1.ts
        Const.dir = Consts1.dir
        Consts0.ts = Consts1.ts
        Consts0.dir = Consts1.dir
        Consts0.dir = Consts1.dir + desplazamiento(Consts1.ts, Consts1.tipo, Consts1.id)
        Consts0.ts = añade(Consts1.ts, Const.id, Const.clase, Const.nivel, Conts0.dir, Const.tipo)

    Consts → Const
        Consts.tsh = Const.tsh
        Consts.dirh = Const.dirh
        Consts.ts = Const.ts
        Consts.dir = Const.ts
        Consts.ts = añade(Const.ts, Const.id, Const.clase, Const.nivel, Const.dir, Const.tipo)


    Const → const TPrim ident asig Lit 
        Const.ts = Const.tsh
        Const.dir = Const.dirh
        Const.id = ident.lex
        Const.clase = const
        Const.nivel = global
        Const.tipo = <t:TPrim.type, tam:1>

    Const → ɛ
        Const.ts = Const.tsh
        Const.dir = Const.dirh

    STypes → tipos illave Types fllave 
        Types.tsh = STypes.tsh
        Types.tsh = STypes.tsh
        STypes.ts = Types.ts 
        STypes.dir = Types.dir 

    STypes → ɛ
        STypes.ts = STypes.tsh
        Types.dir = STypes.dirh

    Types → Types pyc Type 
        Types0.tsh = Types1.tsh
        Types0.dirh = Types1.dirh
        Type.ts = Types1.ts
        Type.dir = Types1.dir
        Types0.ts = Types1.ts
        Types0.dir = Types1.dir
        Types0.dir = Types1.dir + desplazamiento(Types1.ts, Types1.tipo, Types1.id)
        Types0.ts = añade(Types1.ts, Type.id, Type.clase, Type.nivel, Types0.dir, Type.tipo)

    Types → Type
        Types.tsh = Type.tsh
        Types.dirh = Type.dirh
        Types.ts = Type.ts
        Types.dir = Type.ts
        Types.ts = añade(Type.ts, Type.id, Type.clase, Type.nivel, Type.dir, Type.tipo)


    Type → tipo TypeDesc ident 
        Type.ts = Type.tsh
        Type.dir = Type.dirh
        Type.id = ident.lex
        Type.clase = Type
        Type.nivel = global
        Type.tipo = <t:TypeDesc.type, tipo:obtieneCTipo(TypeDesc), tam:desplazamiento(obtieneCTipo(TypeDesc), Type.id)>

    Type → ɛ
        Type.ts = Type.tsh
        Type.dir = Type.dirh

    SVars → vars illave Vars fllave 
        Vars.tsh = SVars.tsh
        Vars.dirh = SVars.dirh
        SVars.ts = Vars.ts
        SVars.dir = Vars.dir

    SVars → ɛ
        SVars.ts = SVars.tsh
        SVars.dir = SVars.dirh

    Vars → Vars pyc Var 
        Vars0.tsh = Vars1.tsh
        Vars0.dirh = Vars1.dirh
        Var.ts = Vars1.ts
        Var.dir = Vars1.dir
        Vars0.ts = Vars1.ts
        Vars0.dir = Vars1.dir
        Vars0.dir = Vars1.dir + desplazamiento(Vars1.tipo, Vars1.id)
        Vars0.ts = añade(Vars1.ts, Var.id, Var.clase, Var.nivel, Vars0.dir, Var.tipo)

    Vars → Var
        Vars.tsh = Var.tsh
        Vars.dirh = Var.dirh
        Vars.ts = Var.ts
        Vars.dir = Var.ts
        Vars.ts = añade(Var.ts, Var.id, Var.clase, Var.nivel, Var.dir, Var.tipo)

    Var → var TypeDesc ident 
        Var.ts = Var.tsh
        Var.dir = Var.dirh
        Var.id = ident.lex
        Var.clase = Var
        Var.nivel = global
        Var.tipo = (si (TypeDesc.Type== TPrim) {<t:TypeDesc.type, tam:1>}
                   si no {<t:ref, id:Var.id, tam: desplazamiento(TypeDesc.tipo, Var.id)>} )

    Var → ɛ
        Var.ts = Var.tsh
        Var.dir = Var.dirh

    SSubprogs → subprograms illave Subprogs fllave | ɛ
    Subprogs → Subprogs Subprog | Subprog
    Subprog → subprogram ident ipar SParams fpar illave SVars SInsts fllaveP