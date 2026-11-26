# Soluciones de programación competitiva en Java: UVa e ICPC

[![CI](https://github.com/KevinAndrey96/java-competitive-programming-solutions/actions/workflows/ci.yml/badge.svg)](https://github.com/KevinAndrey96/java-competitive-programming-solutions/actions/workflows/ci.yml)
![Java 17 | 21 | 25](https://img.shields.io/badge/Java-17%20%7C%2021%20%7C%2025-ED8B00?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-6-25A162?logo=junit5&logoColor=white)
[![Licencia MIT](https://img.shields.io/badge/licencia-MIT-green)](LICENSE)

Soluciones en **Java** a cuatro problemas de **programación competitiva**: dos de **UVa Online Judge** y dos de la **Maratón de Programación ICPC 2019 de la Universidad Nacional de Colombia** (Codeforces Gym 102307). Cada solución lee la entrada estándar en el formato exacto del juez, tiene un análisis de complejidad y se verifica contra un **oráculo de fuerza bruta** con entradas aleatorias.

## Contenido

- [Problemas](#problemas)
- [Uso](#uso)
- [Burger Time? (UVa 11661)](#burger-time-uva-11661)
- [Common Subsequence (Gym 102307C)](#common-subsequence-gym-102307c)
- [Informants (UVa 11659)](#informants-uva-11659)
- [Integer Prefix (Gym 102307I)](#integer-prefix-gym-102307i)
- [Defectos de la versión de 2019 corregidos](#defectos-de-la-versión-de-2019-corregidos)
- [Pruebas y calidad](#pruebas-y-calidad)
- [Historia del proyecto](#historia-del-proyecto)

## Problemas

| Problema | Juez | Técnica | Complejidad |
| --- | --- | --- | --- |
| [Burger Time?](https://onlinejudge.org/external/116/11661.pdf) | UVa 11661 | Barrido lineal | $O(L)$ tiempo, $O(1)$ memoria |
| Common Subsequence | Gym 102307C | Programación dinámica sobre borrados | $O(k^2 + nk)$ con $k = \lfloor n/100 \rfloor$ |
| [Informants](https://onlinejudge.org/external/116/11659.pdf) | UVa 11659 | Fuerza bruta con máscaras de bits | $O(2^i \cdot i)$ |
| Integer Prefix | Gym 102307I | Recorrido de prefijo | $O(n)$ |

Los enunciados de Gym 102307 están en [Codeforces](https://codeforces.com/gym/102307).

## Uso

```bash
mvn -B verify
java -jar target/competitive.jar burger-time < src/test/resources/samples/burger-time.in
```

```text
1
0
7
0
3
2
```

Problemas disponibles: `burger-time`, `common-subsequence`, `informants` e `integer-prefix`. Un problema desconocido o una entrada mal formada terminan con un mensaje en español y código de salida `2`, sin imprimir resultados parciales.

## Burger Time? (UVa 11661)

Una autopista tiene $L$ lugares: restaurantes `R`, droguerías `D`, lugares con ambos `Z` y lugares vacíos `.`. Se pide la distancia mínima entre un restaurante y una droguería.

Si hay una `Z` la respuesta es $0$. Si no, el par óptimo siempre está formado por **dos lugares no vacíos consecutivos**: entre una `R` en $i$ y una `D` en $j$, cualquier lugar no vacío intermedio forma con uno de los extremos un par de tipos distintos y más cercano. Basta recordar el último lugar no vacío visto, así que un solo barrido resuelve cada caso.

## Common Subsequence (Gym 102307C)

Dos cadenas de ADN $A$ y $B$ de longitud $n \le 10^5$ son hermanas si comparten una subsecuencia común de longitud al menos $0.99\,n$. La LCS clásica cuesta $O(n^2) = 10^{10}$ operaciones, demasiado.

La clave es que una subsecuencia común de longitud $\ge 0.99\,n$ descarta como máximo $k = \lfloor n/100 \rfloor$ caracteres de cada cadena. Se define $\mathit{reach}[i][j]$ como la mayor cantidad de caracteres emparejados tras saltar $i$ caracteres de $A$ y $j$ de $B$:

$$
\mathit{reach}[i][j] \leftarrow \text{extender mientras } A[i + m] = B[j + m],
\qquad
\mathit{reach}[i+1][j],\ \mathit{reach}[i][j+1] \leftarrow \max(\cdot,\ \mathit{reach}[i][j])
$$

Emparejar con avidez dos caracteres iguales nunca empeora la solución (la misma idea del algoritmo de diferencias de Myers), y la respuesta es *hermanas* si $100 \cdot \max \mathit{reach} \ge 99\,n$, comparado en enteros para evitar errores de redondeo. La tabla mide $(k+2)^2$ y cada diagonal avanza como máximo $n$ pasos: $O(k^2 + nk) \approx 10^8$ operaciones simples en el peor caso.

## Informants (UVa 11659)

Hay $i \le 20$ informantes y $a \le 800$ afirmaciones del tipo "$x$ dice que $y$ es (o no es) confiable". Un informante confiable solo dice la verdad; uno no confiable puede decir cualquier cosa. Se pide el mayor número de informantes que pueden ser confiables a la vez.

Un conjunto $S$ es consistente si cada miembro $x \in S$ cumple $\mathit{confía}(x) \subseteq S$ y $\mathit{desconfía}(x) \cap S = \varnothing$. Con máscaras de bits cada verificación son dos operaciones por miembro. Los subconjuntos se recorren de mayor a menor tamaño con el truco de Gosper, así que la búsqueda termina en el primer conjunto consistente.

Las muestras del juez no se pueden extraer del PDF oficial, así que el caso de prueba usa el ejemplo del enunciado (respuesta $2$) más dos casos verificados a mano.

## Integer Prefix (Gym 102307I)

Se imprime el prefijo más largo formado solo por dígitos, o `-1` si no existe. El prefijo se copia como texto: puede tener ceros a la izquierda o más dígitos de los que cabe en un `long`, así que nunca se convierte a número.

## Defectos de la versión de 2019 corregidos

| Problema | Defecto original | Corrección |
| --- | --- | --- |
| Burger Time? | Procesaba un solo caso aunque la entrada trae varios hasta `L = 0` | Lee todos los casos |
| Common Subsequence | Buscaba una **subcadena contigua** de longitud $0.99\,n$, no una subsecuencia: rechazaba cadenas hermanas | Programación dinámica sobre borrados |
| Informants | Imprimía el informante con más votos de confianza en lugar del tamaño del mayor conjunto consistente, leía números de un solo dígito y se detenía con casos de `a = 0` | Búsqueda exhaustiva con máscaras de bits y lectura por tokens |
| Integer Prefix | Correcto, pero concatenaba cadenas carácter por carácter, $O(n^2)$ | Un recorrido y un `substring` |

## Estructura

| Clase | Responsabilidad |
| --- | --- |
| `BurgerTime` | Distancia mínima en una pasada |
| `CommonSubsequence` | Decisión de hermandad y LCS de referencia |
| `Informants` | Mayor conjunto consistente con máscaras de bits |
| `IntegerPrefix` | Prefijo numérico más largo |
| `Tokens` | Lectura perezosa de tokens desde la entrada estándar |
| `Main` | Selección del problema, salida y códigos de error |

## Pruebas y calidad

```bash
mvn -B verify   # compila con -Xlint:all -Werror y ejecuta 43 pruebas JUnit
```

- Las muestras de cada juez se ejecutan de punta a punta a través de `Main`.
- `BurgerTime` se compara con la definición cuadrática en 2.000 autopistas aleatorias.
- `CommonSubsequence` se compara con la LCS clásica en 400 pares cerca del umbral del 99 % y se ejecuta con $n = 10^5$ en menos de 5 segundos.
- `Informants` se compara con la enumeración de todos los subconjuntos en 500 encuestas aleatorias.

GitHub Actions compila y prueba en Java 17, 21 y 25, y pasa las muestras por el JAR empaquetado.

## Historia del proyecto

Soluciones de septiembre de 2019 para el curso de algoritmos, escritas en NetBeans. El código original se conserva en la etiqueta [`v0.1-java-coursework-2019`](https://github.com/KevinAndrey96/java-competitive-programming-solutions/tree/v0.1-java-coursework-2019); la carpeta de compilación y la configuración privada de NetBeans se purgaron del historial. En 2026 se reescribieron las soluciones incorrectas y se agregaron pruebas, Maven y CI.

## Autor

**Kevin Andrey Herrera Silva** · [GitHub](https://github.com/KevinAndrey96)

## Licencia

[MIT](LICENSE) © 2019-2026 Kevin Andrey Herrera Silva
