# Conjuntos Difusos — Taller 2
**Fundamentos de Programación Funcional y Concurrente**

---

## Contexto teórico

En la teoría de conjuntos difusos (Zadeh, 1965), la pertenencia no se define de manera binaria (0 o 1), sino de forma gradual dentro del intervalo continuo \( [0,1] \).

Sea un conjunto difuso \( S \subseteq U \) con una **función característica**:

\[
f_S : U \rightarrow [0,1]
\]

donde para cada elemento \( x \in U \):

- \( f_S(x) = 1 \)  →  el elemento **pertenece completamente**.
- \( f_S(x) = 0 \)  →  el elemento **no pertenece**.
- \( 0 < f_S(x) < 1 \)  →  el elemento **pertenece parcialmente**.

---

## Representación funcional en Scala

```scala
type ConjDifuso = Int => Double
```

Con esta abstracción, un conjunto difuso es una función que asocia a cada número entero su grado de pertenencia en el rango \( [0,1] \).

---

## Función `pertenece`

Devuelve el grado exacto de pertenencia de un elemento.

```scala
def pertenece(elem: Int, s: ConjDifuso): Double =
  s(elem)
```

### Ejemplo

```scala
val pares: ConjDifuso = x => if (x % 2 == 0) 1.0 else 0.0
pertenece(4, pares) // → 1.0
pertenece(5, pares) // → 0.0
```

---

## Función `grande(d, e)`

Modela el conjunto difuso de **números grandes** según:

\[
f(n) =
\begin{cases}
0, & n \le 0 \\
\left( \dfrac{n}{n+d} \right)^e, & n > 0
\end{cases}
\]

donde:
- \( d \ge 1 \) controla la **escala** (qué tan rápido crece la pertenencia).
- \( e \ge 1 \) controla la **curvatura** del crecimiento.

### Implementación

```scala
def grande(d: Int, e: Int): ConjDifuso = {
  require(d >= 1, "d debe ser >= 1")
  require(e >= 1, "e debe ser >= 1")
  (n: Int) =>
    if (n <= 0) 0.0
    else math.pow(n.toDouble / (n.toDouble + d.toDouble), e.toDouble)
}
```

---

### Ejemplo numérico

| \( n \) | \( d \) | \( e \) | \( (n/(n+d))^e \) | Resultado | Interpretación |
|:--:|:--:|:--:|:--:|:--:|:--|
| 1 | 1 | 2 | \( (1/2)^2 \) | 0.25 | No es grande |
| 3 | 1 | 2 | \( (3/4)^2 \) | 0.56 | Medianamente grande |
| 10 | 1 | 2 | \( (10/11)^2 \) | 0.826 | Grande |
| 100 | 1 | 2 | \( (100/101)^2 \) | 0.980 | Muy grande |
| 0 | 1 | 2 | \( 0 \) | 0.0 | No pertenece |

---

## Función `complemento`

Devuelve el conjunto difuso complementario:
\[
f_{\neg S}(x) = 1 - f_S(x)
\]

### Implementación

```scala
def complemento(c: ConjDifuso): ConjDifuso = {
  (x: Int) => 1.0 - c(x)
}
```

### Propiedades

- \( f_S(x) + f_{\neg S}(x) = 1 \)
- Es **involutiva**: \( \neg(\neg S) = S \)

---

## Funciones `union` e `interseccion`

\[
f_{S_1 \cup S_2}(x) = \max(f_{S_1}(x), f_{S_2}(x)), \quad  
f_{S_1 \cap S_2}(x) = \min(f_{S_1}(x), f_{S_2}(x))
\]

### Implementaciones

```scala
def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
  (x: Int) => math.max(cd1(x), cd2(x))
}

def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
  (x: Int) => math.min(cd1(x), cd2(x))
}
```

### Propiedades

- Conmutativas: \( A \cup B = B \cup A \), \( A \cap B = B \cap A \)
- Idempotentes: \( A \cup A = A \), \( A \cap A = A \)

---

## Funciones `inclusion` e `igualdad`

### Definiciones

\[
S_1 \subseteq S_2 \iff \forall n \in [0,1000],\ f_{S_1}(n) \le f_{S_2}(n)
\]

\[
S_1 = S_2 \iff (S_1 \subseteq S_2) \land (S_2 \subseteq S_1)
\]

### Implementaciones

```scala
def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
  @annotation.tailrec
  def aux(n: Int): Boolean =
    if (n > 1000) true
    else if (cd1(n) <= cd2(n)) aux(n + 1)
    else false
  aux(0)
}

def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean =
  inclusion(cd1, cd2) && inclusion(cd2, cd1)
```

Estas funciones emplean **recursión de cola**, cumpliendo con los principios de la programación funcional pura.

---

## Conclusiones

- El modelo implementado se ajusta rigurosamente a la definición matemática de Zadeh.
- Todas las funciones son **puramente funcionales**.
- Se verifican sus propiedades mediante pruebas unitarias y límites teóricos.
- El código cumple los criterios exigidos: sin mutabilidad, sin bucles y con recursión segura.

---