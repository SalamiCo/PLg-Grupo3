# 5. Especificación de la traducción

## 5.1 Lenguaje objeto y máquina virtual 

### 5.1.1 Arquitectura

 * Mem: Memoria principal con celdas direccionables con datos. Los datos de la memoria no incluyen información sobre de qué tipo son, las instrucciones sí.

 * Prog: Memoria de programa con celdas direccionables con instrucciones.

 * CProg: Contador de programa con un registro para la dirección de la instrucción actualmente en ejecución

 * Pila: Pila de datos con celdas direccionables con datos. No se incluye información sobre el tipo.

 * CPila: Cima de la pila de datos con un registro para la dirección del dato situado actualmente en la cima de la pila.

 * P: Flag de parada que detiene la ejecución si tiene valor 1.

 * S1: Flag de swap1. Si tiene valor 1 intercambia suma por resta y viceversa.

 * S2: Flag de swap2. Si tiene valor 1 intercambia multiplicación por división y viceversa.

### 5.1.2 Comportamiento interno

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


### 5.1.3 Repertorio de instrucciones

#### Operaciones con la Pila:

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

apila-ret
>Pila[Cpila] ← CProg <br/>
>Cpila ← CPila +1 <br/>
>CProg ← Cprog + 1 <br/>

mueve(nCeldas)
>para i ← 0 hasta nCeldas-1 hacer<br/>
>	Mem[Pila[CPila]+i] ← Mem[Pila[CPila-1]+i]<br/>
>CPila ← Cpila - 2<br/>
>CProg ← CProg + 1<br/>

Nota: Si la dirección de memoria no ha sido cargada previamente con datos usando la siguiente instrucción (desapila-dir), esta instrucción dará un error de ejecución.

ir_ind
>CprogPila[CPila]<br/>
>Cpila←Cpila-1<br/>

desapila-dir(dirección)
>Mem[dirección] ← Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

desapila-ind
>Mem[Pila[CPila]] ← Pila[CPila-1]<br/>
>CPila ← CPila - 2<br/>
>CProg ← CProg + 1<br/>

desapila-ret
>Mem[Pila[Cpila]] ← CProg <br/>
>Cpila ← CPila -1 <br/>
>CProg ← Cprog + 1 <br/>

copia
>CPila ← CPila + 1<br/>
>Pila[CPila] ← Pila[CPila-1]<br/>
>CProg ← CProg + 1<br/>

#### Saltos

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

#### Operaciones aritméticas

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

#### Operaciones de desplazamiento

lsh
>Pila[CPila - 1] ← Pila[CPila - 1] << Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

rsh
>Pila[CPila - 1] ← Pila[CPila - 1] >> Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

#### Operaciones de comparación

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

#### Operaciones lógicas

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

#### Operaciones de conversión

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

#### Operaciones de Entrada-Salida

in(type)
>CPila ← CPila + 1<br/>
>Pila[CPila] ← Leer un valor de tipo type de BufferIN<br/>
>CProg ← CProg + 1<br/>

out
>Escribir en BufferOUT ← Pila[CPila]<br/>
>CPila ← CPila - 1<br/>
>CProg ← CProg + 1<br/>

#### Operaciones de intercambio

swap1
>si S1 = 0: S1 ← 1<br/>
>si S1 = 1: S1 ← 0<br/>

swap2
>si S2 = 0: S2 ← 1<br/>
>si S2 = 1: S2 ← 0<br/>

#### Otras operaciones

range(size)
>si Pila[CPila] < 0 || Pila[CPila] >= size: P ← 1<br/>

stop
>P ← 1<br/>

Consideraciones sobre “Repertorio de instrucciones”

En la operación castNat, hemos creado la operación en la máquina virtual (nat), que no está predefinida en Java, pero cuyo comportamiento está definido en las tablas correspondientes a los tipos definidos.

## 5.2 Funciones semánticas

tamTipo(CTipo): dado un registro de tipo, devuelve el tamaño del tipo
desplTupla(indice, CTipo): dado un registro de tipo y un indice, devuelve el offset hasta el indice (incluido)
numCeldas(CTipo): Dado un tipo te devuelve el numero de celdas de memoria.

## 5.3 Atributos semánticos

 * cod: Atributo sintetizado de generación de código.
 * op: Enumerado que nos dice cuál es el operador utilizado.
 * etq: Contador de instrucciones. Cuenta instucciones de la máquina a pila generadas. 
 * etqh: Contador de instrucciones heredado.  
 * refh: Atributo que indica si la expresión no tiene que generar el apila-ind para cargar el valor. Si la expresión es un parámetro por referencia refh vale true. Si no, vale false.  

## 5.4 Gramática de atributos

	Program → program ident illave SConsts STypes SVars SSubprogs SInsts fllave fin
		Program.cod =  ir_a(SSubprogs.etq) || SSubprogs || SInsts.cod || stop 
		SSubprogs.etqh = 5 /* es 5 por inicializaciones de la pila. */
		SInsts.etqh = SSubprogs.etq

	SSubprogs → subprograms illave Subprogs fllave 
		SSubprogs.cod = Subprogs.cod
		Subprogs.etqh = SSubprogs.etqh
		SSubprogs.etq = Subprogs.etq

	SSubprogs → subprograms illave fllave 
		SSubprogs.cod = [] 
		SSubprogs.etq = SSubprogs.etqh

	SSubprogs → ɛ
		SSubprogs.cod = []
		SSubprogs.etq = SSubprogs.etqh

	Subprogs → Subprogs Subprog 
		Subprogs0.cod  = Subprogs1.cod || Subprog.cod
		Subprogs1.etqh = Subprogs0.etqh
		Subprog.etqh   = Subprogs1.etq 
		Subprogs0.etq  = Subprog.etq

	Subprogs → Subprog
		Subprogs.cod = Subprog.cod
		Subprog.etqh = Subprogs.etqh
		Subprogs.etq = Subprog.etq

	Subprog → subprogram ident ipar SFParams fpar illave SVars SInsts fllave
		Subprog.cod = apila-dir(0) || apila(SVars.dir) || mas || desapila-dir(0) ||
					SInsts.cod || 
					apila_dir(1) || apila(2) || menos || apila_ind || ir_ind
		SInsts.etqh = Subprog.etqh 
		Subprog.etq = SInsts.etq + 5

	SInsts → instructions illave Insts fllave
		SInsts.cod = Insts.cod
		Insts.etqh = SInsts.etqh
		SInsts.etq = Insts.etq

	Insts → Insts pyc Inst 
		Insts0.cod = Insts1.cod || Inst.cod
		Insts1.etqh = Insts0.etqh
		Inst.etqh = Insts1.etq
		Insts0.etq = Inst.etq

	Insts → Inst
		Insts.cod = Inst.cod
		Inst.etqh = Insts.etqh
		Insts.etq = Inst.etq
	 
	Inst → Desig asig Expr
		Inst.cod = Expr.cod || Desig.cod || si esPrimitivo(Desig.tipo) entonces desapila-ind 
					sino mueve(tamTipo(Desig.tipo,Desig.tsh)) 
		Expr.etqh = Inst.etqh
		Desig.etqh = Expr.etq
		Inst.etq = Desig.etq + 1 
		Expr.refh = false

	Inst → in ipar Desig fpar
		Inst.cod = in(Desig.type) ||Desig.cod|| desapila-ind 
		Desig.etqh = Inst.etq + 1 
		Inst.etq = Desig.etq + 1

	Inst → out ipar Expr fpar
		Inst.cod = Expr.cod || out
		Expr.etqh = Inst.etqh
		Inst.etq = Expr.etqh + 1 
		Expr.refh = false

	Inst → swap1 ipar fpar
		Inst.cod = swap1
		Inst.etq = Inst.etqh + 1 

	Inst → swap2 ipar fpar
		Inst.cod = swap2
		Inst.etq = Inst.etqh +1 

	Inst → if Expr then Insts ElseIf
		Inst.cod = Expr.cod || ir_f(Insts.etq + 1) || Insts.cod || ir_a(Elseif.etq) || ElseIf.cod
		Expr.etqh = Inst.etqh
		Insts.etqh = Expr.etq + 1
		ElseIf.etqh = Insts.etq + 1
		Inst.etq = ElseIf.etq
		Expr.refh = false

	Inst → while Expr do Insts endwhile
		Inst.cod = Expr.cod || ir_f(Insts.etq + 1) || Insts.cod || ir_a(Inst.etqh)
		Expr.etqh = Inst.etqh 
		Insts.etqh = Expr.etq + 1
		Inst.etq = Insts + 1 
		Expr.refh = false

	Inst → InstCall
		Inst.cod = IsntCall.cod
		InstCall.etqh = Inst.etqh
		Inst.etq = InstCall.etq

	Inst → ɛ
		Inst.cod = []
		Inst.etq = Inst.etqh

	ElseIf → else Insts endif 
		ElseIf.cod = Inst.cod
		Insts.etqh = ElseIf.etqh
		ElseIf.etq = Insts.etq

	ElseIf → endif
		ElseIf.cod = []
		ElseIf.etq = ElseIf.etqh

	InstCall → call ident lpar SRParams rpar
		InstCall.cod = 
					//Reestructuramos los punteros CP y BASE
					apila-ret || apila-dir(0) || apila(1) || mas || desapila-ind || apiladir(1) || apila-dir(0) || apila(2) || mas || desapila-ind || apila-dir(0) || apila(3) || suma || desapila-dir(0)||
					//Paso de parámetros
					SRParams.cod||
					// Saltar al subprograma
					apila-dir(0) || desapila-dir(1) || apila-dir(0) || apila(tamParametros(InstCall.tsh, ident)) || mas || desapila-dir(0) || ir-ind ||
					//Al volver del subprograma devolver los punteros CP y BASE a su sitio
					apila-dir(1) || apila(3) || menos || desapila-dir(0) || apila-dir(1) || apila(1) || menos || apila-ind || desapila-dir(1)

		SRParams.nparams = 0
		SRParams.etqh = InstCall.etqh + 14 
		InstCall.etq = SRParams.etq + 16

	SRParams → RParams 
		SRParams.cod = RParams.cod
		RParams.etqh = SRParams.etqh
		SRParams.etq = RParams.etq 
		RParams.nparamsh = SRParams.nparamsh
		SRParams.nparams = RParams.nparams

	SRParams → ɛ
		SRParams.cod = []
		SRParms.etq = SRParams.etqh
		SRParams.nparams = SRParams.nparamsh

	RParams → RParams coma RParam 
		RParams0.cod = RParams1.cod || RParam.cod
		RParams1.etqh = RParams0.etqh
		RParam.etqh = RParams1.etq
		RParams.etq = RParam.etq  
		RParams1.nparamsh = RParams0.nparamsh
		RParam.nparamsh = RParams1.nparams
		RParams.nparams = RParam.nparams 

	RParams → RParam 
		RParams.cod = RParam.cod
		RParam.etqh = RParams.etqh
		RParams.etq = RParam.etq
		RParam.nparamsh = RParams.nparamsh
		RParams.nparams = RParam.nparams

	RParam → ident asig Expr
		RParam.cod = Expr.cod || apila_dir(0) || apila(RParams.	nparams) || mas   
					si (RParam.tsh[ident.lex].clase == pvariable)
						|| desapila-ind
					sino si (esPrimitivo(RParam.tsh[ident.lex].tipo)
							|| desapila-ind
						sino // es un tipo compuesto
							|| mueve(tamTipo(RParam.tsh[ident.lex].tipo, Rparam.tsh))

		RParam.nparams = RParams.nparamsh + 1 
		Expr.etqh = RParam.etqh 
		RParam.etq = Expr.etq + 4 
		Expr.refh = RParam.tsh[ident.lex] == pvariable 

	Desig → ident
		Desig.cod = si (Desig.tsh[ident.lex].nivel == global) entonces 
						apila(Desig.tsh[ident.lex].dir)
						Desig.etq = Desig.etq + 1 

					si no // el nivel el local
						si (Desig.tsh[ident.lex].clase == var || Desig.tsh[ident.lex].clase == pvalor) entonces 
							apila_dir(1) || apila(Desig.tsh[ident.lex].dir) || mas
							Desig.etq = Desig.etq + 3 

						si no si (Desig.tsh[ident.lex].clase == pvariable ) 
							apila_dir(1) || apila(Desig.tsh[ident.lex].dir) || mas || apila_ind 
							Desig.etq = Desig.etq + 4 

	Desig → Desig icorchete Expr fcorchete
		Desig0.cod = Desig1.cod || Expr.cod || range(tamTipo(Desig1.type)) || apila(tamTipo(Desig1.type)) || mul || mas
		Desig1.etqh = Desig0.etqh 
		Expr.etqh = Desig1.etq
		Desig0.etq = Expr.etq + 3  
		Expr.refh = false

	Desig → Desig barrabaja litnat		
		Desig0.cod = Desig1.cod || apila(desplTupla(litnat.lex, Desig1.type)) || mas
		Desig1.etqh = Desig0.etqh
		Desig0.etq = Desgi1.etq + 2 

	Expr → Term Op0 Term
		Expr0.cod = Term1.cod || Term2.cod || Op0.op
		Term1.etqh = Expr.etqh 
		Term2.etqh = Term1.etq
		Expr.etq = Term2.etq + 1  
		Term0.refh = Expr.refh
		Term1.refh = Expr.refh

	Expr → Term
		Expr.cod = Term.cod
		Term.etqh = Expr.etqh
		Expr.etq = Term.etq
		Term.refh = Expr.refh

	Term → Term Op1 Fact
		Term0.cod = Term1.cod || Fact.cod || Op1.op
		Term1.etqh = Term0.etqh 
		Fact.etqh = Term1.etq
		Term0.etq = Fact.etq + 1 
		Term1.refh = Term0.refh 
		Fact.refh = Term0.refh

	Term → Term or Fact
		Term0.cod → Term1.cod || copia || ir-v(Fact.etq ) || desapila || Fact.cod 
		Term1.etqh = Term0.etqh 
		Fact.etqh = Term1.etq + 3 
		Term0.etq = Fact.etq  
		Expr.refh = false
		Term1.refh = Term0.refh
		Fact.refh = Term0.refh

	Term → Fact
		Term.cod = Fact.cod
		Fact.etqh = Term.etqh
		Term.etq = Fact.etq
		Fact.refh = Term.refh

	Fact → Fact Op2 Shft
		Fact0.cod = Fact1.cod || Shft.cod || Op2.op
		Fact1.etqh = Fact0.etqh 
		Shft.etqh = Fact1.etq 
		Term0.etq = Shft.etq + 1 
		Fact1.refh = Fact0.refh
		Shft.refh = Fact0.refh

	Fact → Fact and Shft
		Fact0.cod = Fact1.cod || copia || ir-f(Shft.etq ) || desapila || Shft.cod 
		Fact1.etqh = = Fact0.etqh
		Shft.etqh = Fact1.etq + 3
		Fact0.etq = Shft.etq 
		Fact1.refh = Fact0.refh
		Shft.refh = Fact0.refh

	Fact → Shft
		Fact.cod = Shft.cod
		Shft.etqh = Fact.etqh
		Fact.etq = Shft.etq
		Shft.refh = Fact.refh

	Shft → Unary Op3 Shft
		Shft0.cod = Unary.cod || Shft1.cod || Op3.op
		Unary.etqh = Shft0.etqh 
		Shft1.etqh = Unary.etq 
		Shft0.etq = Shft1.etq + 1 
		Unary.refh = Shft0.refh
		Shft1.refh = Shft0.refh

	Shft → Unary
		Shft.cod = Unary.cod
		Unary.etqh = Shft.eqth
		Shft.etq = Unary.etq
		Unary.refh = Shft.refh

	Unary → Op4 Unary
		Unary0.cod = Unary1.cod || Op4.op
		Unary1.etqh = Unary0.eqth
		Unary0.eqt = Unary1.etq + 1 
		Unary1.refh = Unary0.refh

	Unary → lpar Cast rpar Paren
		Unary.cod = Paren.cod || Cast.type
		Paren.etqh = Unary.eqth 
		Unary.etq = Paren.eqt + 1 
		Paren.refh = Unary.refh

	Unary → Paren
		Unary.cod = Paren.cod
		Paren.eqth = Unary.etqh
		Unary.etq = Paren.etq
		Paren.refh = Unary.refh

	Paren → lpar Expr rpar
		Paren.cod = Expr.cod
		Expr.etqh = Paren.eqth
		Paren.etq = Expr.etq
		Expr.tsh = Paren.tsh

	Paren → Lit
		Paren.cod = apila(Lit.valor)
		Paren.etq = Paren.etqh + 1

	Paren → Desig
		Paren.cod = Desig.cod || 
					si (esPrimitivo(Desig.tipo) && Desig.tsh[Desig.lex].clase == constante)
						apila(Desig.tsh[Desig.lex].valor)
						Desig.etq = Desig.etq + 1
					fsi
					si (esPrimitivo(Desig.tipo) && !Paren.refh)
						apila-ind
						Desig.etq = Desig.etq + 1
					fsi
		Desig.etqh = Paren.etqh 
		Paren.etq = Desig.etq + 1 

	Cast → char
		Cast.type = char
	Cast → int
		Cast.type = int
	Cast → nat
		Cast.type = nat
	Cast → float
		Cast.type = float

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

#### Nota:

- Resulta que el codigo de Desig -> Ident está mal. No tiene en cuenta si es una constante. Hay que hacer un apaño y explicar el apaño en algún punto de la memoria. 
