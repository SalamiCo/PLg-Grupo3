
### 4.1. Descripción informal de las restricciones contextuales

Enumeración y descripción de las restricciones contextuales extraídas directamente del enunciado.


#### 4.1.1. Sobre declaraciones

* Las variables, constantes y tipos que se usen en la sección de instrucciones o en la sección de subprogramas habrán debido de ser convenientemente declaradas en su correspondiente sección.
* No se pueden declarar dos variables, constantes o tipos con el mismo identificador.


#### 4.1.2. Sobre instrucciones de asignación

Una instrucción de asignación debe cumplir además estas condiciones:
* La variable en la parte izquierda debe haber sido declarada.
* No se pueden asignar o hacer instrucciones in a constantes.
* A una variable de tipo real es posible asignarle un valor real, entero o natural (produciéndose automáticamente la correspondiente conversión), pero no un carácter, booleano o tipo construido.
* A una variable de tipo entero es posible asignarle un valor entero o natural (produciéndose automáticamente la correspondiente conversión), pero no un valor real, carácter, boolean o tipo construido.
* A una variable de tipo natural únicamente es posible asignarle un valor natural.
* A una variable de tipo carácter únicamente es posible asignarle un valor de tipo carácter.
* A una variable de tipo booleano únicamente es posible asignarle un valor de tipo booleano.
* A una variable de tipo construido únicamente es posible asignarle un valor de ese mismo tipo construido.


#### 4.1.3. Sobre comparaciones

No se puede comparar naturales con caracteres, ni enteros con caracteres, ni reales con caracteres, ni booleanos con caracteres. Tampoco se puede comparar naturales con booleanos, ni enteros con booleanos, ni reales con booleanos, ni caracteres con booleanos.


#### 4.1.4. Sobre operadores

* Los operadores +, -, *, / sólo operan con valores numéricos. No podemos aplicarlos ni a los caracteres, ni a los booleanos ni a los tipos construidos.
* En la operación módulo % el primer operando puede ser entero o natural, pero el segundo operando sólo puede ser natural. El resultado de a % b será el resto de la división de a entre b. El tipo del resultado será el mismo que el del primer operando.
* Los operadores lógicos ‘or’, ‘and’, ‘not’ sólo operan sobre valores booleanos. No podemos aplicarlos ni a los numéricos, ni a los caracteres ni a los tipos construidos.
* Los operadores << y >> sólo operan con valores numéricos naturales.


#### 4.1.5. Sobre operadores de conversión

* (float) puede ser aplicado a cualquier tipo excepto al tipo booleano y a los tipos construidos.
* (int) puede ser aplicado a cualquier tipo excepto al tipo booleano y a los tipos construidos.
* (nat) puede ser aplicado al tipo natural y al tipo carácter. No admite operandos reales, enteros, booleanos o de tipos construidos.
* (char) puede ser aplicado al tipo carácter y al tipo natural. No admite operandos reales, enteros, booleanos o de tipos construidos.

#### 4.1.6 Sobre los subprogramas



##### Sobre la invocación de subprogramas
* No se puede invocar a un subprograma que no esté previamente declarado
* Hay que comprobar que los parámetros reales con los que se invoca al subprograma son correctos. Es decir, comprobar que:
	* Se invoque con el mismo número de parámetros que el declarado en la cabecera del subprograma. 
	* Que cada parámetro se invoque con un identificador que haya sido declarado en la cabecera. 
	* Que no haya dos parámetros reales invocados con el mismo identificador
	* Comprobar que, cuando pasamos un parámetro por referencia, sea un designador. 
	* Que los parámetros que pasamos estén previamente declarados en la table de símbolos 
	* Que los parámetros reales que pasemos sean compatibles con el tipo del parámetros formal declarado en la cabecera de la función. 


##### Sobre la declaración de subprogramas hay que comprobar
* Que no declaremos dos parámetros formales de entrada, con el mismo identificador. 
* Que no haya un subprograma declarado previamente con el mismo identificador. 

 