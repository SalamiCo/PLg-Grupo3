package plg.pruebas;

import es.ucm.fdi.plg.evlib.TAtributos;
import es.ucm.fdi.plg.evlib.Atributo;

import java.io.FileInputStream;
import java_cup.runtime.*;


public class Main {
  public static void main(String[] args) throws Exception {   
      Atributo.fijaDebug(false); // se desactiva la traza de evaluación
      @SuppressWarnings("deprecation")
	  Parser p = new Parser(new Scanner(new FileInputStream("input.txt")),new DefaultSymbolFactory());
      Symbol s = p.parse();

      System.out.println("RESULTADO:");
      System.out.println("==============================");     
      System.out.println(((TAtributos) s.value).a("id").valor());
      System.out.println("==============================");
  }
}
