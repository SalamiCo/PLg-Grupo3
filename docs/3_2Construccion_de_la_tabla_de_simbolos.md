## 3.2.1 Funciones semánticas

creaTS() : TS
> Crea una tabla de símbolos vacía. 

creaTS(ts:TS) : TS
> Dada una tabla de símbolos crea otra tabla de símbolos que contiene toda la información de la tabla recibida por parámetro. Esta constructora se usa para las tablas de símbolos de los subprogramas

añade(ts:TS, id:String, clase:String, nivel:String, dir:Int, tipo:CTipo, valor:?) : TS
>Añade a la tabla de símbolos el nuevo tipo construido, una variable o una constante. CTipo es el conjunto de propiedades con la información necesaria del tipo. Está explicado más adelante.

campo?(ts:TS, campos:CCampo, id:String) : Boolean
>Devuelve true cuando la lista de campos de Campo contenga campo id. 

desplazamiento(tipo:CTipo, id:String) : Integer
>Devuelve el tamaño que ocupa en memoria el identificador id. Si no hay un identificador con ese nombre devuelve terr

existeID(ts:TS, id:String) : Boolean
>Dada una tabla de símbolos y el campo id de un identificador, indica si el identificador existe en la tabla de símbolos (sensible a mayúsculas y minúsculas), es decir, si ha sido previamente declarado.

obtieneCtipo(typeDesc:TypeDesc) : CTipo
>Dado un descriptor de tipos devuelve el CTipo asociado

obtieneTipoString(ident:String) : String
>Dado un identificador, devuelve su tipo en un String.

stringToNat(v:String) : Natural 
>Convierte el atributo pasado como string a un valor natural.

stringToFloat(v:String) : Float 
>Convierte el atributo pasado como string a un valor decimal.

stringToChar(v:String) : Character 
>Convierte el atributo pasado como string a un carácter


## CTipo
CTipo es el conjunto de propiedades con la información necesaria del tipo. CTipo guarda información diferente dependiendo de si es un tipo construido, un array, una tupla, una variable de todo lo anterior dicho o bien una variable o constante de tipo básico. 

### CTipo en tipos construidos
Cuando la tabla de símbolos guarda un tipo construido, el campo tipo guarda la siguiente información.

    <id:String, t:reg, tipo:Ctipo, tam:int>

### CTipo en arrays
Cuando la tabla de símbolos guarda un array, el campo tipo guarda la siguiente información. 

    <id:String, t:array, nelems:int, tbase:Ctipo, tam:int>

### Ctipo en tuplas
Cuando la tabla de símbolos guarda un array el campotipo guarda la siguiente información.  

    <id:String, t:tupla, nelems:int, campos:CCampos, tam:int>

Donde `campos` es una lista de elementos de la forma:

    <id:int, tipo:CTipo>

### Ctipo en variables cuando guardan una referencia a otro tipo
Cuando la tabla de símbolos guarda una variable, con una referencia a otro tipo, el campo tipo guarda la siguiente información. 

    <id:String, t:ref, id:String, tam:int>

### Ctipo en constantes y variables que guardan un tipo primitivo
Cuando la tabla de símbolos guarda una constante o, una variable con tipos primitivos, el campo tipo guarda la siguiente información. 

    <t:int, tam:1> 
    <t:nat, tam:1>
    <t:float, tam:1>
    <t:bool, tam:1>
    <t:char, tam:1>

### Ctipo en subprogramas
Cuando la tabla de símbolos guarda la cabecera de un subprograma, el campo tipo guarda la siguiente información.

    <id:String, t:subprog, params[...]>

La lista `params` guarda los parámetros de entrada que recibe el subprograma. Se distinguen entre los parámetros que son por valor o los que son por referencia. El campo idparam es el string que identifica el parámetro al hacer la llamada al subprograma.  

    <tipo:CTipo, modo:valor, idparam:String>
    <tipo:CTipo, modo:variable, idparam:String>


## 3.2.2 Atributos semánticos

**ts:** tabla de símbolos sintetizada

**id:** nombre del identificador

**clase:** Indica si es la declaración de un tipo construido, una variable, una constante, un subprograma, un parámetro por valor o un parámetro por referencia.

**nivel** Indica si el identificador es de ámbito global o local

**dir:** Dirección de memoria. Dónde se guarda la variable o la constante

**tipo** Almacena los conjuntos de propiedades con la información necesaria del tipo

**valor:** Si es una constante, almacena su valor. Si no, es indefinido.

### 3.2.3 Gramáticas de atributos

A continuación se detalla la construcción de los atributos relevantes para la creación de la tabla de símbolos. Otros atributos, como la tabla de símbolos heredada (que tan solo se propaga) o el tipo y el valor de las expresiones se detallarán más adelante en sus correspondientes secciones.

La tabla de símbolos comienda a guardar las declaraciones a partir de la dirección 0 de memoria. Ya que la dirección 0 está reservada para la `cima de la pila` y la dirección 1 para la `base`. La base apunta al inicio de los datos del procedimiento actualmente activo. 

    Program → program ident illave SConsts STypes SVars SSubprogs SInsts fllave fin
        Program.tsh = creaTS()
        Program.dirh = 2
        SConsts.tsh = Program.tsh
        SConsts.dirh = Program.dirh
        STypes.tsh = SConsts.ts
        STypes.dirh = SConsts.dir
        SVars.tsh = STypes.ts
        SVars.dirh = STypes.dir
        SSubprogs.tsh = SVars.ts
        SSubprogs.dirh = SVars.dir
        SInsts.tsh = SVars.ts
        SVars.nivelh = global

    SConsts → const illave Consts fllave 
        Consts.tsh = SConsts.tsh
        Consts.dirh = SConsts.dirh
        SConsts.ts = Consts.ts
        SConsts.dir = Consts.dir

    SConsts → ɛ
        SConsts.ts = SConsts.tsh
        SConsts.dir = SConsts.dirh

    Consts → Consts pyc Const
        Consts1.tsh = Consts0.tsh
        Consts1.dirh = Consts0.dirh
        Const.tsh = Consts1.ts
        Const.dirh = Consts1.dir
        Consts0.dir = Const.dir + desplazamiento(Const.tipo, Const.id)
        Consts0.ts = añade(Const.ts, Const.id, Const.clase, Const.nivel, Conts0.dir, Const.tipo, Const.valor)

    Consts → Const
        Const.tsh = Consts.tsh
        Const.dirh = Consts.dirh
        Const.nivelh = Consts.nivelh
        Consts.dir = Const.dir + desplazamiento(Const.tipo, Const.id)
        Consts.ts = añade(Const.ts, Const.id, Const.clase, Const.nivel, Const.dir, Const.tipo, Const.valor)


    Const → const TPrim ident asig ConstLit 
        Const.ts = Const.tsh
        Const.dir = Const.dirh
        Const.id = ident.lex
        Const.clase = const
        Const.nivel = global
        Const.tipo = <t:TPrim.tipo, tam:1>
        Const.valor = ConstLit.valor

    Const → ɛ
        Const.ts = Const.tsh
        Const.dir = Const.dirh

    ConstLit → Lit
        ConstLit.valor = Lit.valor

    ConstLit → menos Lit
        ConstList.valor = -(Lit.valor)

    STypes → tipos illave Types fllave 
        Types.tsh = STypes.tsh
        Types.dirh = STypes.dirh
        STypes.ts = Types.ts 
        STypes.dir = Types.dir 

    STypes → ɛ
        STypes.ts = STypes.tsh
        STypes.dir = STypes.dirh

    Types → Types pyc Type 
        Types1.tsh = Types0.tsh
        Types1.dirh = Types0.dirh
        Type.tsh = Types1.ts
        Type.dirh = Types1.dir
        Types0.dir = Type.dir + desplazamiento(Type.tipo, Types0.id)
        Types0.ts = añade(Types1.ts, Type.id, Type.clase, Type.nivel, Types0.dir, Type.tipo)

    Types → Type
        Type.tsh = Types.tsh
        Type.dirh = Types.dirh
        Types.dir = Type.dir + desplazamiento(Type.tipo, Type.id)
        Types.ts = añade(Type.ts, Type.id, Type.clase, Type.nivel, Type.dir, Type.tipo)


    Type → tipo TypeDesc ident 
        Type.ts = Type.tsh
        Type.dir = Type.dirh
        Type.id = ident.lex
        Type.clase = Tipo
        Type.tipo = <t:TypeDesc.type, tipo:obtieneCTipo(TypeDesc), tam:desplazamiento(obtieneCTipo(TypeDesc), Type.id)> //TODO mirar como añadir el tamaño al tipo

    Type → ɛ
        Type.ts = Type.tsh
        Type.dir = Type.dirh

    SVars → vars illave Vars fllave 
        Vars.tsh = SVars.tsh
        Vars.dirh = SVars.dirh
        Vars.nivelh = SVars.nivelh
        SVars.ts = Vars.ts
        SVars.dir = Vars.dir

    SVars → ɛ
        SVars.ts = SVars.tsh
        SVars.dir = SVars.dirh

    Vars → Vars pyc Var 
        Vars1.tsh = Vars0.tsh
        Vars1.dirh = Vars0.dirh
        Vars1.nivelh = Vars0.nivelh
        Var.nivelh = Vars0.nivelh
        Var.tsh = Vars1.ts
        Var.dirh = Vars1.dir
        Vars0.dir = Var.dir + desplazamiento(Var.tipo, Vars1.id)
        Vars0.ts = añade(Var.ts, Var.id, Var.clase, Var.nivel, Vars0.dir, Var.tipo)

    Vars → Var
        Var.tsh = Vars.tsh
        Var.dirh = Vars.dirh
        Var.nivelh = Vars.nivelh
        Vars.dir = Var.dir + desplazamiento(Var.tipo, Var.id)
        Vars.ts = añade(Var.ts, Var.id, Var.clase, Var.nivel, Var.dir, Var.tipo)

    Var → var TypeDesc ident 
        Var.ts = Var.tsh
        Var.dir = Var.dirh
        Var.id = ident.lex
        Var.clase = Var
        Var.nivel = Var.nivelh
        Var.tipo = (si (TypeDesc.tipo == TPrim) {<t:TypeDesc.tipo, tam:1>}
                   si no {<t:ref, id:Var.id, tam: desplazamiento(TypeDesc.tipo, Var.id)>} )

    Var → ɛ
        Var.ts = Var.tsh
        Var.dir = Var.dirh

    SSubprogs → subprograms illave Subprogs fllave 
        Subprogs.tsh = SSubprogs.tsh

    SSubprogs → subprograms illave fllave 

    SSubprogs → ɛ

    Subprogs → Subprogs Subprog
        Subprogs1.tsh =  Subprogs0.tsh
        Subprog.tsh = Subprogs0.tsh   

    Subprogs → Subprog
        Subprog.tsh = Subprogs.tsh

    Subprog → subprogram ident ipar SFParams fpar illave SVars SInsts fllave
        SFParams.dirh = 0
        SFParams.tsh = CreaTS(añade(ident, subprog, global, ? , TODO))
        SVars.tsh = SFParams.ts
        SVars.dirh = SFParams.dir
        SInsts.tsh = SVars.ts
        SVars.nivelh = local

    SFParams → FParams 
        FParams.tsh = SFParams.tsh
        SFParams.ts = FParams.ts
        FParams.dirh = SFParams.dirh
        SFParams.dir = FParams.dir

    SFParams → ɛ
        SFParams.ts = SFParams.tsh
        SFParams.dir = SFParams.dirh

    FParams → FParams coma FParam 
        FParams1.tsh = FParams0.tsh
        FParams1.dirh = FParams0.dirh
        FParam.tsh = FParams1.tsh
        FParam.dirh = FParams1.dirh
        FParams0.dir = FParam.dir + desplazamiento(FParam.tipo, FParam.id) 
        FParams0.ts = añade(FParam.ts, FParam.id, FParam.clase, FParam.nivel, FParam.dir, FParam.tipo)

    FParams → FParam
        FParam.dirh = FParams.dirh
        FParam.tsh = FParams.tsh
        FParams.ts = añade(FParam.ts, FParam.id, FParam.clase, FParam.nivel, FParam.dir, FParam.tipo)
        FParams.dir = FParam.dir + desplazamiento(FParam.tipo, FParam.id)

    FParam → TypeDesc ident 
        FParam.ts = FParam.tsh
        FParam.dir = FParam.dirh 
        Fparam.id = ident.lex
        FParam.clase = pvalor
        FParam.nivel = local
        FParam.tipo = (si (TypeDesc.tipo== TPrim) {<t:TypeDesc.tipo, tam:1>}
                   si no {<t:ref, id:FParam.id, tam: desplazamiento(TypeDesc.tipo, Param.id)>} )

    FParam → TypeDesc mul ident
        FParam.ts = FParam.tsh
        FParam.dir =  FParam.dirh 
        Fparam.id = ident.lex
        FParam.clase = pvariable
        FParam.nivel = local
        FParam.tipo = (si (TypeDesc.tipo == TPrim) {<t:TypeDesc.tipo, tam:1>}
                   si no {<t:ref, id:FParam.id, tam: 1>} )

    TTupla → ipar Tupla fpar
        Tupla.tsh = TTupla.tsh
        TTupla.tipo = Tupla.tipo

    TTupla → ipar fpar

    Tupla → TypeDesc coma Tupla
        TypeDesc.tsh = Tupla0.tsh
        Tupla1.tsh = Tupla0.tsh
        Tupla.tipo = TypeDesc.tipo ++ Tupla.tipo

    Tupla → TypeDesc
        TypeDesc.tsh = Tupla.tsh
        Tupla.tipo = TypeDesc.tipo

    Lit → LitBool 
        Lit.valor = LitBool.valor

    Lit → LitNum
        Lit.valor = LitNum.valor

    Lit → litChar
        Lit.valor = stringToChar(litchar)

    LitBool → true 
        LitBool.valor = true

    LitBool → false
        LitBool.valor = false

    LitNum → litNat
        LitNum.valor = stringToNat(litnat)

    LitNum → litFloat
        LitNum.valor = stringToFloat(litfloat)
