
### 4.2 Funciones semánticas

A continuación, describimos las funciones semánticas adicionales utilizadas en la descripción.

    casting (Type tipoCast, Type tipoOrg) : Type
        Dados dos tipos diferentes comprobamos si podemos hacer el casting: [ (tipoCast) tipoOrg ] Si podemos, devolvemos el tipoCast resultante de hacer el casting, y si no podemos, devolvemos terr. Describimos el comportamiento de la función en la siguiente tabla.

|    TipoCast     |         TipoOrg           | Tipo devuelto |
|:---------------:|:-------------------------:|:-------------:|￼
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
| tipo construido | -                         | terr          |

    unario(Type OpUnario, Type tipoUnario) : Type
        Dado un operador unario y el tipo al que es aplicado comprobamos si se puede aplicar. Por ejemplo, no podemos aplicar a un booleano el operador “-”. Tampoco podemos aplicar a un entero el operador “not”. En esos casos devuelve terr. Si aplicamos el operador “-” a un tipo nat devolvemos el tipo integer.
￼￼￼￼

|  OpUnario  |  tipoUnario          | Tipo devuelto |
|:----------:|:--------------------:|:-------------:|
|     "-"    |  natural             |    integer    |
|     "-"    |  integer             |    integer    |
|     "-"    |  float               |     float     |
|     "-"    |  cualquier otro tipo |     terr      |
|     not    |  boolean             |   boolean     |
|     not    |  cualquier otro tipo |     terr      |

    tipoFunc(Type tipo1, Operator op, Type tipo2) : Type
        Dados dos tipos diferentes y un operador comprobamos que los tipos puedan aplicar el operador. Devolvemos el tipo correspondiente al aplicar el operador. Si el operador no puede ser aplicado entonces devolvemos terr.

Si pusiésemos todas las posibilidades la tabla resultante quedaría muy extensa. Para simplificar, se pondrán dos tablas. En la primera, se pondrán los operadores conmutativos. Es decir, aquellos que se comportan igual sean los tipos asignados al primer parámetro de la función o al segundo. En la segunda se pondrán los operadores no conmutativos. En los que importa quién sea el tipo1 y el tipo2.

También para que se vea mejor, dentro de las tablas, separaremos los tipos de operadores. Operadores conmutativos:

| Tipo1               |             Op               |         Tipo2           | Tipo devuelto |
|:-------------------:|:----------------------------:|:-----------------------:|:-------------:|
| tipo numérico       | cualquier op. de comparación | tipo numérico           | boolean       |
| boolean             | cualquier op. de comparación | boolean                 | boolean       |
| character           | cualquier op. de comparación | character               | boolean       |
| tipo construido     | cualquier op. de comparación | -                       | terr          |
| boolean             | cualquier op. aritmética     | -                       | terr          |
| character           | cualquier op. aritmética     | -                       | terr          |
| float               | cualquier op. aritmética     | cualquier tipo numérico | float         |
| integer             | cualquier op. aritmética     | integer o natural       | integer       |
| natural             | cualquier op. aritmética     | natural                 | natural       |
| tipo construido     | cualquier op. aritmética     | -                       | terr          |
| boolean             | cualquier op. lógica         | boolean                 | boolean       |
| cualquier otro tipo | cualquier op. lógica         | -                       | terr          |
| natural             | "<<"                         | natural                 | natural       |
| natural             | ">>"                         | natural                 | natural       |
| tipo no natural     | "<<"                         | -                       | terr          |
| -                   | "<<"                         | tipo no natural         | terr          |


Operadores no conmutativos:

|         Tipo1         | Op  |     Tipo2       | Tipo devuelto |
|:---------------------:|:---:|:---------------:|:-------------:|
| integer o natural     | "%" | natural         | natural       |
| -                     | "%" | tipo no natural | terr          |
| ni integer ni natural | "%" | -               | terr          |

    asignaciónVálida(Type tipoVar, Type tipoExp) : Boolean
        Dado un tipo de una variable y un tipo de una expresión, comprueba si a la variable se le asigna un tipo permitido. Por ejemplo, no podemos asignar a una variable de tipo char una expresión booleana. Si la asignación es incorrecta devolvemos false, si no devolvemos true.

Para que se vea mejor, dentro de las tablas, separaremos los tipos posibles de tipoVar.

| TipoVar   | TipoExp             | Tipo devuelto |
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

Nota: En el caso de los tipos construidos, devolverá true siempre que el tipo de la expresión sea el mismo tipo construido, y las componentes de ambos del mismo tipo. En cualquier otro caso, devolverá false.


Nota: En todas las funciones, si alguno de los tipos de entrada es el tipo terr, devolvemos siempre terr.



// TODO 
	esVariable()
	tamañoCorrecto() -> Revisa como hacer esto en condiciones, porque creo que es mas jodido de lo que parece. Cómo miras el tamaño del array o la tupla?
	Revisar por encima lo que has dejado a medias cuando te has ido por si acaso me he colado en algo.
	Revisar los Designadores porque no me cuadran del todo.    