package taller

class ConjuntosDifusos {
  type ConjDifuso = Int => Double

  def pertenece(elem: Int, s: ConjDifuso): Double =
    s(elem)

  def grande(d: Int, e: Int): ConjDifuso = {
    require(d >= 1, "d debe ser >= 1")
    require(e >= 1, "e debe ser >= 1")
    (n: Int) =>
      if (n <= 0) 0.0
      else math.pow(n.toDouble / (n.toDouble + d.toDouble), e.toDouble)
  }

  def complemento(c: ConjDifuso): ConjDifuso = {
    (x: Int) => 1.0 - c(x)
  }

  def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (x: Int) => math.max(cd1(x), cd2(x))
  }

  def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (x: Int) => math.min(cd1(x), cd2(x))
  }

  def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {

    @annotation.tailrec
    def aux(n: Int): Boolean = {
      if (n > 1000) true                                // Caso base: recorrimos todo el universo
      else if (cd1(n) <= cd2(n)) aux(n + 1)             // Sigue verificando
      else false                                        // Si encuentra un valor que no cumple, corta
    }

    aux(0)
  }

  def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    inclusion(cd1, cd2) && inclusion(cd2, cd1)
  }

}
