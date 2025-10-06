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
}
