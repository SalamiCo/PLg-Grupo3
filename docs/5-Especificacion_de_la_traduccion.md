# 5. Especificación de la traducción

### 5.1.1. Arquitectura

 * Mem: Memoria principal con celdas direccionables con datos. Los datos de la memoria no incluyen información sobre de qué tipo son, las instrucciones sí.

 * Prog: Memoria de programa con celdas direccionables con instrucciones.

 * CProg: Contador de programa con un registro para la dirección de la instrucción actualmente en ejecución

 * Pila: Pila de datos con celdas direccionables con datos. No se incluye información sobre el tipo.

 * CPila: Cima de la pila de datos con un registro para la dirección del dato situado actualmente en la cima de la pila.

 * P: Flag de parada que detiene la ejecución si tiene valor 1.

 * S1: Flag de swap1. Si tiene valor 1 intercambia suma por resta y viceversa.

 * S2: Flag de swap2. Si tiene valor 1 intercambia multiplicación por división y viceversa.


### 5.1.2.  Comportamiento interno

Pseudocódigo del algoritmo de su ejecución:

>CPila ← -1<br/>
>CProg ← 0<br/>
>S1 ← 0<br/>
>S2 ← 0<br/>
>P ← 0<br/>
>mientras P = 0<br/>
>    ejecutar Prog[CProg]<br/>
>fmientras<br/>

 * Mem[dirección]: Dato de una celda de memoria principal localizado a través de una dirección.

 * Prog[dirección]: Instrucción de una celda de memoria de programa localizado a través de una dirección.

La dirección -1 en CPila indica que la pila está vacía.


### 5.1.3.  Repertorio de instrucciones

 * Operaciones con la Pila:

apila(valor)
>CPila ← CPila + 1<br/>
>Pila[CPila] ← valor<br/>
>CProg ← CProg + 1<br/>

apila-dir(dirección)
>CPila ← CPila + 1<br/>
>Pila[CPila] ← Mem[dirección]<br/>
>CProg ← CProg + 1<br/>

apila-ind
>Pila[CPila] ← Mem[Pila[CPila]]<br/>
>CProg ← CProg + 1<br/>

mueve(nCeldas)
>para i ← 0 hasta nCeldas-1 hacer<br/>
>	Mem[Pila[CPila-1]+i] ← Mem[Pila[CPila]+i]<br/>
>CPila ← Cpila - 2<br/>
>CProg ← CProg + 1<br/>

Nota: Si la dirección de memoria no ha sido cargada previamente con datos usando la siguiente instrucción (desapila-dir), esta instrucción dará un error de ejecución.

desapila-dir(dirección)
>Mem[dirección] ← Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

//REVISARRRRR
desapila-ind
>Reg ← Mem[direccion]<br/>
>Mem[Pila[CPila-2]] ← Pila[CPila]<br/>
>CPila ← CPila - 2<br/>
>CProg ← CProg + 1<br/>

copia
>CPila ← CPila + 1<br/>
>Pila[CPila] ← Pila[CPila-1]<br/>
>CProg ← CProg + 1<br/>

 * Saltos

 ir-a(direccion)
 >CProg ← direccion<br/>

 ir-v(direccion)
 >si Pila[CPila]: CProg ← direccion<br/>
 >si no: CProg ← CProg + 1<br/>
 >CPila ← CPila-1<br/>

 ir-f(direccion)
 >si Pila[CPila]: CProg ← CProg + 1<br/>
 >si no: CProg ← direccion<br/>
 >CPila ← CPila-1<br/>

 * Operaciones aritméticas

mas
>si S1 = 0: Pila[CPila - 1] ← Pila[CPila - 1] + Pila[CPila]<br/>
>si S1 = 1: Pila[CPila - 1] ← Pila[CPila - 1]  - Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

menos (binario)
>si S1 = 0: Pila[CPila - 1] ← Pila[CPila - 1] - Pila[CPila]<br/>
>si S1 = 1: Pila[CPila - 1] ← Pila[CPila - 1] + Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

mul
>si S2 = 0: Pila[CPila - 1] ← Pila[CPila - 1] * Pila[CPila]<br/>
>si S2 = 1: Pila[CPila - 1] ← Pila[CPila - 1] / Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

div
>si S2 = 0: Pila[CPila - 1] ← Pila[CPila - 1] / Pila[CPila]<br/>
>si S2 = 1: Pila[CPila - 1] ← Pila[CPila - 1] * Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

mod
>Pila[CPila - 1] ← Pila[CPila - 1] % Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

menos (unario)
>Pila[CPila] ← - Pila[CPila]<br/>
>CProg ← CProg + 1<br/>

 * Operaciones de desplazamiento

lsh
>Pila[CPila - 1] ← Pila[CPila - 1] << Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

rsh
>Pila[CPila - 1] ← Pila[CPila - 1] >> Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

 * Operaciones de comparación

igual
>Pila[CPila - 1] ← Pila[CPila - 1] == Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

noigual
>Pila[CPila - 1] ← Pila[CPila - 1] != Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

may
>Pila[CPila - 1] ← Pila[CPila - 1] > Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

men
>Pila[CPila - 1] ← Pila[CPila - 1] < Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

mayoig
>Pila[CPila - 1] ← Pila[CPila - 1] >= Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CPoprog + 1<br/>

menoig
>Pila[CPila - 1] ← Pila[CPila - 1] <= Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

 * Operaciones lógicas

and
>Pila[CPila - 1] ← Pila[CPila - 1] && Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

or
>Pila[CPila - 1] ← Pila[CPila - 1] || Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

not
>Pila[CPila] ← ! Pila[CPila]<br/>
>CProg ← CProg + 1<br/>

 * Operaciones de conversión

castFloat
>Pila[CPila] ← (float) Pila[CPila]<br/>
>CProg ← CProg + 1<br/>

castInt
>Pila[CPila] ← (int) Pila[CPila]<br/>
>CProg ← CProg + 1<br/>

castNat
>Pila[CPila] ← (nat) Pila[CPila]<br/>
>CProg ← CProg + 1<br/>

castChar
>Pila[CPila] ← (char) Pila[CPila]<br/>
>CProg ← CProg + 1<br/>

 * Operaciones de Entrada-Salida

in(type)
>CPila ← CPila + 1<br/>
>Pila[CPila] ← Leer un valor de tipo type de BufferIN<br/>
>CProg ← CProg + 1<br/>

out
>Escribir en BufferOUT ← Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

 * Operaciones de intercambio

swap1
>si S1 = 0: S1 ← 1<br/>
>si S1 = 1: S1 ← 0<br/>

swap2
>si S2 = 0: S2 ← 1<br/>
>si S2 = 1: S2 ← 0<br/>

 * Otras operaciones

stop
>P ← 1<br/>

Consideraciones sobre “Repertorio de instrucciones”

En la operación castNat, hemos creado la operación en la máquina virtual (nat), que no está predefinida en Java, pero cuyo comportamiento está definido en las tablas correspondientes a los tipos definidos.

## 5.2. Funciones semánticas

No hacemos uso de ninguna función semántica. 

## 5.3. Atributos semánticos

 * cod: Atributo sintetizado de generación de código.
 * op: Enumerado que nos dice cuál es el operador utilizado.

## 5.4. Gramática de atributos

Program → program ident illave SConsts STypes SVars SSubprogs SInsts fllave fin
	Program.cod = ir_a(?) || stack_pointer || SSubprogs || SInsts.cod || stop

SSubprogs → subprograms illave Subprogs fllave 
	SSubprogs.cod = Subprogs.cod

SSubprogs → subprograms illave fllave 
	SSubprogs.cod = [] 

SSubprogs → ɛ
	SSubprogs.cod = []

Subprogs → Subprogs Subprog 
	Subprogs0.cod = Subprogs1.cod || Subprog.cod

Subprogs → Subprog
	Subprogs.cod = Subprog.cod

Subprog → subprogram ident ipar SParams fpar illave SVars SInsts fllave
	Subprog.cod = gen_prologo || SInsts.cod || gen_epilogo

SInsts → instructions illave Insts fllave
	SInsts.cod = Insts.cod

Insts → Insts pyc Inst 
	Insts0.cod = Insts1.cod || Inst.cod

Insts → Inst
	Insts.cod = Inst.cod

Inst → Desig asig Expr
	Inst.cod = apila(Desig.dir) || Expr.cod || desapila-ind

Inst → in ipar Desig fpar
	Inst.cod = in(Desig.type) || desapila-dir(Desig.dir) 

Inst → out ipar Expr fpar
	Inst.cod = Expr.cod || out

Inst → swap1 ipar fpar
	Inst.cod = swap1

Inst → swap2 ipar fpar
	Inst.cod = swap2

Inst → if Expr then Insts ElseIf
	Inst.cod = Expr.cod || ir_f(Insts.dir) || Insts.cod || ir_a(Elseif.dir) || ElseIf.cod

Inst → while Expr do Insts endwhile
	Expr.dirh = Inst.dirh
	Inst.cod = Expr.cod || ir_f(Insts.dir) || Insts.cod || ir_a(Expr.dirh) 

Inst → InstCall
	Inst.cod = 
Inst → ɛ
ElseIf → else Insts endif | endif
InstCall → call ident lpar SRParams rpar

SRParams → RParams | ɛ
RParams → RParams coma RParam | RParam
RParam → ident asig Expr

Desig → ident | Desig icorchete Expr fcorchete | Desig barrabaja litnat

Expr → Term Op0 Term | Term
Term → Term Op1 Fact | Fact
Fact → Fact Op2 Shft | Shft
Shft → Unary Op3 Shft | Unary
Unary → Op4 Unary | lpar Cast rpar Paren | Paren

Paren → lpar Expr rpar
Paren → Lit
Paren → Desig

Op0 → igual
	Op0.op = igual
Op0 → noigual
	Op0.op = noigual
Op0 → men
	Op0.op = men
Op0 → may
	Op0.op = may
Op0 → menoig
	Op0.op = menoig
Op0 → mayoig
	Op0.op = mayoig
Op1 → menos
	Op1.op = menos
Op1 → mas
	Op1.op = mas
Op2 → mod
	Op2.op = mod
Op2 → div
	Op2.op = div
Op2 → mul
	Op2.op = mul

Op3 → lsh
	Op3.op = lsh
Op3 → rsh
	Op3.op = rsh
Op4 → not
	Op4.op = not
Op4 → menos
	Op4.op = menos
