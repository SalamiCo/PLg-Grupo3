
    Program → program ident illave SConsts STypes SVars SSubprogs SInsts fllave fin
        Program.tsh = creaTS()
        SConsts.tsh = Program.tsh
        STypes.tsh = SConsts.ts
        SVars.tsh = STypes.ts
        SSubprogs.tsh = SVars.ts
        SInsts.tsh = SVars.ts
        Program.err = SConsts.err ∨ STypes.err ∨ SVars.err ∨ SSubprogs.err ∨ SInsts.err

    SConsts → const illave Consts fllave 
        Consts.tsh = SConsts.tsh
        SConsts.ts = Consts.ts
        SConsts.err = Consts.err

    SConsts → ɛ
        SConsts.ts = SConsts.tsh
        SConsts.err = false

    Consts → Consts pyc Const
        Consts1.tsh = Consts0.tsh
        Const.tsh = Consts1.ts
        Consts0.ts = añade(Const.ts, Const.id, Const.clase, Const.nivel, Conts0.dir, Const.tipo)
        Consts.err = existe(Const.ts, Const.id)

    Consts → Const
        Const.tsh = Consts.tsh
        Consts.ts = añade(Const.ts, Const.id, Const.clase, Const.nivel, Const.dir, Const.tipo)
        Consts.err = existe(Const.ts, Const.id)

    Const → const TPrim ident asig Lit 
        Const.ts = Const.tsh
        Const.id = ident.lex
        Const.clase = const
        Const.nivel = global
        Const.tipo = <t:TPrim.type, tam:1>

    Const → ɛ
        Const.ts = Const.tsh
        Const.err = false

    STypes → tipos illave Types fllave 
        Types.tsh = STypes.tsh
        STypes.ts = Types.ts
        STypes.err = Types.err

    STypes → ɛ
        STypes.ts = STypes.tsh
        STypes.err = false

    Types → Types pyc Type 
        Types1.tsh = Types0.tsh
        Type.tsh = Types1.ts
        Types0.ts = añade(Types1.ts, Type.id, Type.clase, Type.nivel, Types0.dir, Type.tipo)
        Types0.err = existe(Types1.ts, Type.id)

    Types → Type
        Type.tsh = Types.tsh
        Types.ts = añade(Type.ts, Type.id, Type.clase, Type.nivel, Type.dir, Type.tipo)
        Types.err = existe(Type.ts, Type.id)

    Type → tipo TypeDesc ident 
        Type.ts = Type.tsh
        Type.id = ident.lex
        Type.clase = Tipo
        Type.nivel = global
        Type.tipo = <t:TypeDesc.type, tipo:obtieneCTipo(TypeDesc), tam:desplazamiento(obtieneCTipo(TypeDesc), Type.id)> //TODO mirar como añadir el tamaño al tipo

    Type → ɛ
        Type.ts = Type.tsh
        Type.err = false

    SVars → vars illave Vars fllave 
        Vars.tsh = SVars.tsh
        SVars.ts = Vars.ts
        SVars.err = Vars.err

    SVars → ɛ
        SVars.ts = SVars.tsh
        SVars.err = false

    Vars → Vars pyc Var 
        Vars1.tsh = Vars0.tsh
        Var.tsh = Vars1.ts
        Vars0.ts = añade(Var.ts, Var.id, Var.clase, Var.nivel, Vars0.dir, Var.tipo)
        Vars0.err = existe(Var.ts, Var.id)

    Vars → Var
        Var.tsh = Vars.tsh
        Vars.ts = añade(Var.ts, Var.id, Var.clase, Var.nivel, Var.dir, Var.tipo)
        Vars.err = existe(Var.ts, Var.id)

    Var → var TypeDesc ident 
        Var.ts = Var.tsh
        Var.id = ident.lex
        Var.clase = Var
        Var.nivel = global
        Var.tipo = (si (TypeDesc.Type == TPrim) {<t:TypeDesc.type, tam:1>}
                   si no {<t:ref, id:Var.id, tam: desplazamiento(TypeDesc.tipo, Var.id)>} )

    Var → ɛ
        Var.ts = Var.tsh
        Var.err = false

    SSubprogs → subprograms illave Subprogs fllave 
        Subprogs.tsh = SSubprogs.tsh
        SSubprogs.err = Subprogrs.err

    SSubprogs → subprograms illave fllave 

    SSubprogs → ɛ
        SSubprogs = false

    Subprogs → Subprogs Subprog
        Subprogs1.tsh = Subprogs0.tsh
        Subprog.tsh = Subprogs0.tsh
        Subprogs0.err = Subprogs1.err ∨ Subprog.err

    Subprogs → Subprog
        Subprog.tsh = Subprogs.tsh
        Subprogs.err = Subprog.err

    Subprog → subprogram ident ipar SParams fpar illave SVars SInsts fllave
        SParams.tsh = CreaTS(añade(ident, subprog, global, ? , TODO))
        SVars.tsh = SParams.ts
        SInsts.tsh = SVars.ts
        Subprog.err = existe(Subprog.tsh, ident) ∨ SParams.err ∨ SVars.err ∨ SInsts.err

    SParams → FParams 
        FParams.tsh = SParams.tsh
        SParams.ts = FParams.ts
        SParams.dir = FParams.dir
        Sparams.err = FParams.err

    SParams → ɛ
        SParams.ts = SParams.tsh
        SParams.err = false

    FParams → FParams coma FParam 
        FParams1.tsh = FParams0.tsh
        FParam.tsh = FParams1.tsh
        FParams0.ts = añade(FParam.ts, FParam.id, FParam.clase, FParam.nivel, FParam.dir, FParam.tipo)
        FParams0.err = existe(FParam.ts, FParam.id)

    FParams → FParam
        FParam.tsh = FParams.tsh
        FParams.ts = añade(FParam.ts, FParam.id, FParam.clase, FParam.nivel, FParam.dir, FParam.tipo)
        FParams.err = existe(FParam.ts, FParam.id)

    FParam → TypeDesc ident 
        FParam.ts = FParam.tsh
        Fparam.id = ident.lex
        FParam.clase = pvalor
        FParam.nivel = local
        FParam.tipo = (si (TypeDesc.Type== TPrim) {<t:TypeDesc.type, tam:1>}
                   si no {<t:ref, id:FParam.id, tam: desplazamiento(TypeDesc.tipo, Param.id)>} )

    FParam → TypeDesc mul ident
        FParam.ts = FParam.tsh
        Fparam.id = ident.lex
        FParam.clase = pvariable
        FParam.nivel = local
        FParam.tipo = (si (TypeDesc.Type== TPrim) {<t:TypeDesc.type, tam:1>}
                   si no {<t:ref, id:FParam.id, tam: desplazamiento(TypeDesc.tipo, Param.id)>} )

    TypeDesc → TPrim | TArray | TTupla | ident

	TPrim → natural | integer | float | boolean | character
	Cast → char | int | nat | float

	TArray → TypeDesc icorchete ident fcorchete | TypeDesc icorchete litnat fcorchete

	TTupla → ipar Tupla fpar | ipar fpar
	Tupla → TypeDesc coma Tupla | TypeDesc

	SInsts → instructions illave Insts fllave
		Insts.tsh = SInsts.tsh
		SInsts.err = Insts.err

	Insts → Insts pyc Inst
		Insts1.tsh = Insts0.tsh
		Inst.tsh = Insts0.tsh
		SInsts.err = Insts.err ∨ Inst.err

	Insts → Inst
		Inst.tsh = Insts.tsh
		Insts.err = Inst.err

	Inst → Desig asig Expr
		Desig.tsh = Inst.tsh
		Expr.tsh = Inst.tsh
		Inst.err = (¬asignacionValida(Desig.type, Expr.type)) ∨ Expr.err ∨ Desig.err

	Inst → in ipar Desig fpar
		Desig.tsh = Inst.tsh
		Inst.err = Desig.err

	Inst → out ipar Expr fpar
		Expr.tsh = Inst.tsh
		Inst.err = Expr.err

	Inst → swap1 ipar fpar
		Inst.err = false

	Inst → swap2 ipar fpar
		Inst.err = false

	Inst → if Expr then Insts ElseIf
		Expr.tsh = Inst.tsh
		Insts.tsh = Inst.tsh
		ElseIf.tsh = Inst.tsh
		Inst.err = Expr.err ∨ Insts.err ∨ ElseIf.err

	Inst → while Expr do Insts endwhile
		Expr.tsh = Inst.tsh
		Insts.tsh = Inst.tsh
		Inst.err = Expr.err ∨ Insts.err

	Inst → InstCall
		InstCall.tsh = Inst.tsh
		Inst.err = InstCall.err

	Inst → ɛ
		Inst.err = false

	ElseIf → else Insts endif
		Insts.tsh = ElseIf.tsh
		ElseIf.err = Insts.err

	ElseIf → endif
		ElseIf.err = false

	InstCall → call ident lpar SRParams rpar
		SRParams.tsh = InstCall.tsh
		InstCall.err = SRParams.err

	SRParams → RParams
		RParams.tsh = SRParams.tsh
		SRParams.err = RParams.err

	SRParams → ɛ
		SRParams.err = false

	RParams → RParams coma RParam
		RParam1.tsh = RParams0.tsh
		RParam.tsh = RParams1.tsh
		RParams0.err = RParams1.err ∨ Rparam.err

	RParams → RParam
		RParams.err = RParam.err

	RParam → ident asig Expr
		Expr.tsh = RParam.tsh
		RParam.err = Expr.err ∨ ¬asignaciónVálida(Expr.tsh[ident.lex].type, Expr.type) ∨ ¬existe(Exp.tsh, ident.lex) ∨ 
                        ¬esVariable(Expr.tsh, ident.lex)

	Desig → ident
		Desig.type = Desig.tsh[ident.lex].type
		Desig.err = ¬existe(Desig.tsh, ident) ∨ ¬esVariable(Expr.tsh, ident.lex)

	Desig → Desig icorchete Expr fcorchete
		Desig0.type = Desig1.type
		Desig0.err = Desig1.err ∨ Expr.err ∨ ¬tamañoCorrecto()

	Desig → Desig barrabaja litnat
		Desig0.type = Desig1.type
		Desig0.err = Desig1.err ∨ ¬tamañoCorrecto()

	Expr → Term Op0 Term
		Expr.type = tipoFunc(Term0.type, Op0.op, Term1.type)
		Term0.tsh = Expr.tsh
		Term1.tsh = Expr.tsh
	
	Expr → Term
		Expr.type = Term.type
		Term.tsh = Expr.tsh

	Term → Term Op1 Fact 
		Term0.type = tipoFunc(Term1.type, Op1.op, Fact.type)
		Term1.tsh = Term0.tsh
		Fact.tsh = Term0.tsh

	Term → Term or Fact
		Term0.type = tipoFunc(Term1.type, or, Fact.type)
		Term1.tsh = Term0.tsh
		Fact.tsh = Term0.tsh
	
	Term → Fact
		Term.type = Fact.type
		Fact.tsh = Term.tsh
	
	Fact → Fact Op2 Shft
		Fact0.type = tipoFunc(Fact1.type, Op2.op, Shft.type) 
		Fact1.tsh = Fact0.tsh
		Shft.tsh = Fact0.tsh

	Fact → Fact and Shft
		Fact0.type = tipoFunc(Fact1.type, and, Shft.type)
		Fact1.tsh = Fact0.tsh
		Shft.tsh = Fact0.tsh
	
	Fact → Shft
		Fact.type = Shft.type
		Shft.tsh = Fact.tsh
	
	Shft → Unary Op3 Shft
		Shft0.type = tipoFunc(Unary.type, Op3.op, Shft.type) 
        Unary.tsh = Shft0.tsh
		Shft1.tsh = Shft0.tsh
	
	Shft → Unary
		Shft.type = Unary.type
		Unary.tsh = Shft.tsh
	
	Unary → Op4 Unary
		Unary0.type = opUnario(Op4.op, Unary1.type)
		Unary1.tsh = Unary0.tsh
	
	Unary → lpar Cast rpar Paren 
		Unary.type = casting(Cast.type, Paren.type)
		Paren.tsh = Unary.tsh
	
	Unary → Paren
		Unary.type = Paren.type
		Paren.tsh = Unary.tsh
	
	Paren → lpar Expr rpar 
		Paren.type = Expr.type
		Expr.tsh = Paren.tsh
	
	Paren → Lit 
		Parent.type = Lit.type
		Lit.tsh = Paren.tsh
	
	Paren → ident
		Paren.type = tipoDe(Paren.tsh, ident.lex)

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

	Lit → LitBool 
		Lit.type = boolean 

	Lit → LitNum 
		Lit.type = LitNum.type

	Lit → litchar 
		Lit.type = char 

	LitNum → litnat 
		LitNum.type = natural 

	LitNum → menos litnat 
		LitNum.type = integer

	LitNum → litfloat | menos litfloat 
		LitNum.type = float

### Notas Marina

- En el enunciado pone "En las expresiones básicas, se substituye el uso de variables por el de  designadores (es decir, donde en las expresiones de la versión anterior se podía utilizar una variable, ahora es posible utilizar un designador). " Algunas definiciones que hay en el 4.2 han de cambiar en consecuentas
