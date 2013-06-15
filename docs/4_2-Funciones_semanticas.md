
### 4.2 Funciones semánticas

A continuación, describimos las funciones semánticas adicionales utilizadas en la descripción.

#### casting

    casting (Type tipoCast, Type tipoOrg) : Type
        Dados dos tipos diferentes comprobamos si podemos hacer el casting: [ (tipoCast) tipoOrg ] Si podemos, devolvemos el tipoCast resultante de hacer el casting, y si no podemos, devolvemos terr. Describimos el comportamiento de la función en la siguiente tabla.

|    TipoCast     |         TipoOrg           | Tipo devuelto |
|:---------------:|:-------------------------:|:-------------:|
|  natural        | natural                   | natural       |
|  natural        | character                 | natural       |
|  natural        | cualquier otro tipo       | terr          |
|  boolean        | -                         | terr          |
| character       | character                 | character     |
| character       | natural                   | character     |
| character       | cualquier otro tipo       | terr          |
|  integer        | boolean                   | terr          |
|  integer        | tipo númerico o character | integer       |
|   float         | boolean                   | terr          |
|   float         | tipo númerico o character | terr          |

Nota: cualquier casting en el que esté involucrado un tipo construido da como tipo devuelto 'terr'.

#### unario

    unario(Type OpUnario, Type tipoUnario) : Type
        Dado un operador unario y el tipo al que es aplicado comprobamos si se puede aplicar. Por ejemplo, no podemos aplicar a un booleano el operador “-”. Tampoco podemos aplicar a un entero el operador “not”. En esos casos devuelve terr. Si aplicamos el operador “-” a un tipo nat devolvemos el tipo integer.

|  OpUnario  |  tipoUnario          | Tipo devuelto |
|:----------:|:--------------------:|:-------------:|
|     "-"    |  natural             |    integer    |
|     "-"    |  integer             |    integer    |
|     "-"    |  float               |     float     |
|     "-"    |  cualquier otro tipo |     terr      |
|     not    |  boolean             |   boolean     |
|     not    |  cualquier otro tipo |     terr      |

Nota: no se puede aplicar ningún operador unario a ningún tipo construido.

#### tipoFunc

    tipoFunc(Type tipo1, Operator op, Type tipo2) : Type
        Dados dos tipos diferentes y un operador comprobamos que los tipos puedan aplicar el operador. Devolvemos el tipo correspondiente al aplicar el operador. Si el operador no puede ser aplicado entonces devolvemos terr.

Si pusiésemos todas las posibilidades la tabla resultante quedaría muy extensa. Para simplificar, se pondrán dos tablas. En la primera, se pondrán los operadores conmutativos. Es decir, aquellos que se comportan igual sean los tipos asignados al primer parámetro de la función o al segundo. En la segunda se pondrán los operadores no conmutativos. En los que importa quién sea el tipo1 y el tipo2.

También para que se vea mejor, dentro de las tablas, separaremos los tipos de operadores. Operadores conmutativos:

| Tipo1               |             Op               |         Tipo2           | Tipo devuelto |
|:-------------------:|:----------------------------:|:-----------------------:|:-------------:|
| tipo numérico       | cualquier op. de comparación | tipo numérico           | boolean       |
| boolean             | cualquier op. de comparación | boolean                 | boolean       |
| character           | cualquier op. de comparación | character               | boolean       |
| boolean             | cualquier op. aritmética     | -                       | terr          |
| character           | cualquier op. aritmética     | -                       | terr          |
| float               | cualquier op. aritmética     | cualquier tipo numérico | float         |
| integer             | cualquier op. aritmética     | integer o natural       | integer       |
| natural             | cualquier op. aritmética     | natural                 | natural       |
| boolean             | cualquier op. lógica         | boolean                 | boolean       |
| cualquier otro tipo | cualquier op. lógica         | -                       | terr          |
| natural             | "<<"                         | natural                 | natural       |
| natural             | ">>"                         | natural                 | natural       |
| tipo no natural     | "<<"                         | -                       | terr          |
| -                   | "<<"                         | tipo no natural         | terr          |

Nota: el tipo devuelto de aplicar cualquier tipo de operador a un tipo construido es 'terr'.


Operadores no conmutativos:

|         Tipo1         | Op  |     Tipo2       | Tipo devuelto |
|:---------------------:|:---:|:---------------:|:-------------:|
| integer o natural     | "%" | natural         | natural       |
| -                     | "%" | tipo no natural | terr          |
| ni integer ni natural | "%" | -               | terr          |

#### asignaciónVálida

    asignaciónVálida(Type tipoDesig, Type tipoExp) : Boolean
        Dado un tipo de un designador y un tipo de una expresión, comprueba si ambos son tipos compatibles. Por ejemplo, no podemos asignar a un designador de tipo char una expresión booleana. Si la asignación es incorrecta devolvemos false, si no devolvemos true.

Para que se vea mejor, dentro de las tablas, separaremos los tipos posibles de tipoDesig.

| TipoDesig | TipoExp             | Tipo devuelto |
|:---------:|:-------------------:|:-------------:|
| natural   | natural             | true          |
| natural   | cualquier otro tipo | false         |
| integer   | natural             | true          |
| integer   | integer             | true          |
| integer   | cualquier otro tipo | false         |
| float     | tipo numérico       | true          |
| float     | cualquier otro tipo | false         |
| boolean   | boolean             | boolean       |
| boolean   | cualquier otro tipo | false         |
| character | character           | true          |
| character | cualquier otro tipo | false         |

Nota: En el caso de los tipos construidos, devolverá true siempre que los dos tipos sean compatibles, y false en c.o.c. Dos tipos se consideran compatibles cuando el tipo de sus componentes es el mismo y, en el caso de los arrays, su tamaño es el mismo.

#### esVariable
    esVariable(TS ts, String id) : Boolean
        Indica si el ident dado, representado por su id, es una variable o una constante. Si devuelve true quiere decir que el ident es una variable, si devuelve false quiere decir que el identificador es una constante.

#### existe
    existe(TS ts, String id) : Boolean
        Indica si el identificador existe en la tabla de símbolos

    existe(TS ts, String is, nivel) : Boolean
        Indica si el identificador existe en la tabla de símbolos en el nivel inidicado. 

### numParametros
    numParametros(TS ts, String id) : Integer
        Devuelve el número de parámetros que tiene el subprograma con el identificador id. Si el subprograma no está en la tabla del símbolos devuelve terr.

### estaDeclarado
    estaDeclarado(TS ts, String idparam, String idsubprog) : Boolean
        Comprueba si el parámetro idparam está declarado en el subprograma idsubprog. Si no está declarado el identificador, o el subprograma no existe devuelve terr

### compatible
	compatible(CTipo, CTipo) : Boolean
		Dados dos tipos nos indica si son campatibles entre ellos		

### parametrosNoRepetidos
    parametrosNoRepetidos(TS ts, String id)
        Dado el nombre de un identificador de un subprograma "id" comprobamos que no hay dos identificadores de parámetros en la cabecera con el mismo nombre.   

### esPrimitivo
	esPrimitivo(Ctipo c)
		Dado un tipo te dice si es un tipo primitivo.
		
### tamTipo(Ctipo c, TS ts)
		Dado un tipo te devuelve e tamaño mirandolo en la tabla de simbolos.

#### Nota: 
En todas las funciones, si alguno de los tipos de entrada es el tipo terr, devolvemos siempre terr.