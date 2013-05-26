
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
