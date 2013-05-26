
    Program → program ident illave SConsts STypes SVars SSubprogs SInsts fllave fin
        Program.tsh = creaTS()
        SConsts.tsh = Program.tsh
        STypes.tsh = SConsts.ts
        SVars.tsh = STypes.ts
        SSubprogs.tsh = SVars.ts
        SInsts.tsh = SVars.ts

    SConsts → const illave Consts fllave 
        Consts.tsh = SConsts.tsh
        SConsts.ts = Consts.ts

    SConsts → ɛ
        SConsts.ts = SConsts.tsh

    Consts → Consts pyc Const
        Consts1.tsh = Consts0.tsh
        Const.tsh = Consts1.ts
        Consts0.ts = añade(Const.ts, Const.id, Const.clase, Const.nivel, Conts0.dir, Const.tipo)

    Consts → Const
        Const.tsh = Consts.tsh
        Consts.dir = Const.dir + desplazamiento(Const.tipo, Const.id)
        Consts.ts = añade(Const.ts, Const.id, Const.clase, Const.nivel, Const.dir, Const.tipo)


    Const → const TPrim ident asig Lit 
        Const.ts = Const.tsh
        Const.id = ident.lex
        Const.clase = const
        Const.nivel = global
        Const.tipo = <t:TPrim.type, tam:1>

    Const → ɛ
        Const.ts = Const.tsh

    STypes → tipos illave Types fllave 
        Types.tsh = STypes.tsh
        STypes.ts = Types.ts 

    STypes → ɛ
        STypes.ts = STypes.tsh

    Types → Types pyc Type 
        Types1.tsh = Types0.tsh
        Type.tsh = Types1.ts
        Types0.ts = añade(Types1.ts, Type.id, Type.clase, Type.nivel, Types0.dir, Type.tipo)

    Types → Type
        Type.tsh = Types.tsh
        Types.ts = añade(Type.ts, Type.id, Type.clase, Type.nivel, Type.dir, Type.tipo)


    Type → tipo TypeDesc ident 
        Type.ts = Type.tsh
        Type.id = ident.lex
        Type.clase = Tipo
        Type.nivel = global
        Type.tipo = <t:TypeDesc.type, tipo:obtieneCTipo(TypeDesc), tam:desplazamiento(obtieneCTipo(TypeDesc), Type.id)> //TODO mirar como añadir el tamaño al tipo

    Type → ɛ
        Type.ts = Type.tsh

    SVars → vars illave Vars fllave 
        Vars.tsh = SVars.tsh
        SVars.ts = Vars.ts

    SVars → ɛ
        SVars.ts = SVars.tsh

    Vars → Vars pyc Var 
        Vars1.tsh = Vars0.tsh
        Var.tsh = Vars1.ts
        Vars0.ts = añade(Var.ts, Var.id, Var.clase, Var.nivel, Vars0.dir, Var.tipo)

    Vars → Var
        Var.tsh = Vars.tsh
        Vars.ts = añade(Var.ts, Var.id, Var.clase, Var.nivel, Var.dir, Var.tipo)

    Var → var TypeDesc ident 
        Var.ts = Var.tsh
        Var.id = ident.lex
        Var.clase = Var
        Var.nivel = global
        Var.tipo = (si (TypeDesc.Type == TPrim) {<t:TypeDesc.type, tam:1>}
                   si no {<t:ref, id:Var.id, tam: desplazamiento(TypeDesc.tipo, Var.id)>} )

    Var → ɛ
        Var.ts = Var.tsh

    SSubprogs → subprograms illave Subprogs fllave 
        Subprogs.tsh = SSubprogs.tsh

    SSubprogs → subprograms illave fllave 

    SSubprogs → ɛ

    Subprogs → Subprogs Subprog
        Subprogs1.tsh =  Subprogs0.tsh
        Subprog.tsh = Subprogs0.tsh   

    Subprogs → Subprog
        Subprog.tsh = Subprogs.tsh

    Subprog → subprogram ident ipar SParams fpar illave SVars SInsts fllave
        SParams.tsh = CreaTS(añade(ident, subprog, global, ? , TODO))
        SVars.tsh = SParams.ts
        SInsts.tsh = SVars.ts

    SParams → FParams 
        FParams.tsh = SParams.tsh
        SParams.ts = FParams.ts
        SParams.dir = FParams.dir

    SParams → ɛ
        SParams.ts = SParams.tsh

    FParams → FParams coma FParam 
        FParams1.tsh = FParams0.tsh
        FParam.tsh = FParams1.tsh
        FParams0.ts = añade(FParam.ts, FParam.id, FParam.clase, FParam.nivel, FParam.dir, FParam.tipo)

    FParams → FParam
        FParam.tsh = FParams.tsh
        FParams.ts = añade(FParam.ts, FParam.id, FParam.clase, FParam.nivel, FParam.dir, FParam.tipo)

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
