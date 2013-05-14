package plg.pruebas;

import es.ucm.fdi.plg.evlib.Atribucion;
import es.ucm.fdi.plg.evlib.Atributo;
import es.ucm.fdi.plg.evlib.SemFun;
import es.ucm.fdi.plg.evlib.TAtributos;

// Definición de las funciones semánticas
class InitTrue implements SemFun{
    @Override
    public Object eval(Atributo... args) {
        return "[True]";
    }
}

class InitFalse implements SemFun{
    @Override
    public Object eval(Atributo... args) {
        return "[False]";
    }
}

class InitNat implements SemFun{
    @Override
    public Object eval(Atributo... args) {
        return "[Nat:"+(String)args[0].valor()+"]";
    }
}

class InitFloat implements SemFun{
    @Override
    public Object eval(Atributo... args) {
        return "[Float:"+(String)args[0].valor()+"]";
    }
}

class InitChar implements SemFun{
    @Override
    public Object eval(Atributo... args) {
        return "[Char:"+(String)args[0].valor()+"]";
    }
}

class InitIdent implements SemFun{
    @Override
    public Object eval(Atributo... args) {
        return "[Ident:"+(String)args[0].valor()+"]";
    }
}

class Concatenacion implements SemFun{
    @Override
    public Object eval(Atributo... args) {
        return (String)args[0].valor()+"||"+(String)args[1].valor();
    }
}

class Asignacion implements SemFun{
    @Override
    public Object eval(Atributo... args) {
        return args[0].valor();
    }
}


public class EAtribucion extends Atribucion {    
    
    // Se crean los objetos que representan las diferentes funciones semánticas
    private static SemFun asignacion = new Asignacion();
    private static SemFun concatenacion = new Concatenacion();
    private static SemFun initTrue = new InitTrue();
    private static SemFun initFalse = new InitFalse();
    private static SemFun initNat = new InitNat();
    private static SemFun initFloat = new InitFloat();
    private static SemFun initChar = new InitChar();
    private static SemFun initIdent = new InitIdent();
    
    
    // Se especifican las funciones de atribución que se emplearán en la definición 
    // CUP. Estas habrá que substituirlas por las correctas, programadas
    // según los patrones dados en clase

//    public TAtributos sR1(TAtributos s1, TAtributos i){
//        regla("S -> S I");
//        
//        TAtributos s = atributosPara("S", "id");
//        dependencias(s.a("id"), s1.a("id"), i.a("id"));
//        
//        calculo(s.a("id"), concatenacion); 
//        
//        return s;
//    }
    
    public TAtributos sR1(TAtributos i){
        regla("S -> I");        
        TAtributos s = atributosPara("S", "id");
        dependencias(s.a("id"), i.a("id"));        
        calculo(s.a("id"), asignacion);        
        return s;
    }
        
    public TAtributos iR1(){
        regla("I -> TRUE");        
        TAtributos i = atributosPara("I", "id");      
        calculo(i.a("id"), initTrue);        
        return i;
    }
    
    public TAtributos iR2(){
        regla("I -> FALSE");        
        TAtributos i = atributosPara("I", "id");      
        calculo(i.a("id"), initFalse);        
        return i;
    }
    
    public TAtributos iR3(String litnat){
        regla("I -> LITNAT"); 
        Atributo lexNat = atributoLexicoPara("LITNAT", "lex", litnat);
        TAtributos i = atributosPara("I", "id"); 
        dependencias(i.a("id"), lexNat);             
        calculo(i.a("id"), initNat);        
        return i;
    }
    
    public TAtributos iR4(String litfloat){
        regla("I -> LITFLOAT");
        Atributo lexFloat = atributoLexicoPara("LITFLOAT", "lex", litfloat);
        TAtributos i = atributosPara("I", "id"); 
        dependencias(i.a("id"), lexFloat);             
        calculo(i.a("id"), initFloat);        
        return i;
    }
    
    public TAtributos iR5(String litchar){
    	regla("I -> LITCHAR"); 
        Atributo lexChar = atributoLexicoPara("LITCHAR", "lex", litchar);
        TAtributos i = atributosPara("I", "id"); 
        dependencias(i.a("id"), lexChar);             
        calculo(i.a("id"), initChar);        
        return i;
    }
    
    public TAtributos iR6(String ident){
    	regla("I -> IDENT"); 
        Atributo lexIdent = atributoLexicoPara("IDENT", "lex", ident);
        TAtributos i = atributosPara("I", "id"); 
        dependencias(i.a("id"), lexIdent);             
        calculo(i.a("id"), initIdent);        
        return i;
    }
}
