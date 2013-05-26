Program → program ident illave SDecs SInsts fllave fin SInts.tsh = SDecs.ts
Program.err = SDecs.err ∨ SInsts err
SDecs → varconsts illave Decs fllave
      SDecs.ts = Decs.ts
      SDecs.err = Decs.err
Decs → Decs pyc Dec
Decs0.ts = AñadeID( Decs1.ts, Dec.id, Dec.type, Dec.const, Dec.valor )
Decs0.err = ExisteID(Decs1.ts, Dec.id) Decs → Dec
Decs.ts = AñadeID( CreaTS(), Dec.id, Dec.type, Dec.const, Dec.valor )
Dec → var Type ident
Dec → const Type ident dpigual Lit Dec → ɛ
SInsts → instructions illave Insts fllave Insts.tsh = SInsts.tsh
       SInsts.err = Insts.err
Insts → Insts pyc Inst
       Inst.tsh = Insts0.tsh
       Insts1.tsh = Insts0.tsh
Insts0.err = Insts1.err ∨ Inst.err Insts → Inst
       Inst.tsh = Insts.tsh
       Insts.err = Inst.err
Inst → ident asig Expr
       Expr.tsh = Inst.tsh
Inst.err = ( ¬asignaciónVálida(Inst.tsh[ident.lex].type, Expr.type) ∨¬existeID(Inst.tsh, ident.lex) ∨ Inst.tsh[ident.lex].const = true)
Inst → in lpar ident rpar
Inst.err = (¬existeID(Inst.tsh, ident.lex) ∨ Inst.tsh[ident.lex].const = true)
Type → boolean
       Type.type = boolean
Type → character
       Type.type = chararacter
Type → integer
       Type.type = integer
Type → natural
       Type.type = natural
Type → float
       Type.type = float
Cast → char
       Cast.type = char
Cast → int
       Cast.type = int
Cast → nat
       Cast.type = nat
Cast → float
       Cast.type = float
Expr → Term Op0 Term
Expr.type = tipoFunc(Term0.type,Op0.op,Term1.type)
      Term0.tsh = Expr.tsh
      Term1.tsh = Expr.tsh
Expr → Term
       Expr.type = Term.type
      Term.tsh = Expr.tsh
￼Inst → out lpar Expr rpar
￼Inst.err = (Expr.type == terr)
Term → Term Op1 Fact
Term0.type = tipoFunc(Term1.type, Op1.op, Fact.type)
      Term1.tsh = Term0.tsh
      Fact.tsh = Term0.tsh
Term → Fact
       Term.type = Fact.type
      Fact.tsh = Term.tsh
Fact → Fact Op2 Shft
Fact0.type = tipoFunc(Fact1.type, Op2.op, Shft.type)
      Fact1.tsh = Fact0.tsh
      Shft.tsh = Fact0.tsh
Fact → Shft
       Fact.type = Shft.type
       Shft.tsh = Fact.tsh
Shft → Unary Op3 Shft
Shft0.type = tipoFunc(Unary.type, Op3.op, Shft.type) Shft1.tsh = Shft0.tsh
Unary.tsh = Shft0.tsh
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
Paren →  lpar Expr rpar
       Paren.type = Expr.type
       Expr.tsh = Paren.tsh
Paren → Lit
       Parent.type = Lit.type
       Lit.tsh = Paren.tsh
Paren → ident
Paren.type = tipoDe(ident.lex, Paren.tsh)
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
Op1 → or
       Op1.op = or
Op1 → menos
       Op1.op = menos
Op1 → mas
       Op1.op = mas
Op2 → and
       Op2.op = and
Op2 → mod
       Op2.op = mod
Op2 → div
       Op2.op = div
Op2 → mul
       Op2.op = mul
Op3 → lsh
       Op3.op = lsh
Op3 →rsh
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