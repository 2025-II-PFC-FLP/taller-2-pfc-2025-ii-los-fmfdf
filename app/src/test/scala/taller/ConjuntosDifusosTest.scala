package taller

import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
final class ConjuntosDifusosTest extends AnyFunSuite {

  // ============================================================
  //                   PRUEBAS (PERTENECE Y GRANDE)
  // ============================================================

  val m = new ConjuntosDifusos(); import m._

  test("pertenece aplica la función característica") {
    val pares: ConjDifuso = x => if (x % 2 == 0) 1.0 else 0.0
    assert(pertenece(4, pares) === 1.0)
    assert(pertenece(5, pares) === 0.0)
  }

  test("grande(d=1,e=2) coincide con (n/(n+1))^2 para n>0 y es 0 para n<=0") {
    val g = grande(1, 2)
    assert(math.abs(g(1) - 0.25) < 1e-9)
    assert(g(100) > 0.97 && g(100) < 1.0)
    assert(g(0) === 0.0)
    assert(g(-5) === 0.0)
  }

  test("pertenece respeta exactamente la función característica") {
    val ident: ConjDifuso = x => (x / 10.0).max(0.0).min(1.0) // cualquier función [0,1]
    assert(pertenece(0, ident) === 0.0)
    assert(math.abs(pertenece(5, ident) - 0.5) < 1e-9)
    assert(pertenece(20, ident) === 1.0) // cap en 1.0
  }

  test("grande(d=1,e=1) es creciente en n>0") {
    val g = grande(1, 1)
    assert(g(2) > g(1))
    assert(g(10) > g(2))
  }

  test("grande: a mayor d, menor pertenencia (mismo n,e)") {
    val g1 = grande(1, 2)
    val g5 = grande(5, 2)
    val n  = 10
    assert(g1(n) > g5(n))
  }

  test("grande: a mayor e, menor pertenencia si base<1 (mismo n,d)") {
    val g2 = grande(2, 1)
    val g4 = grande(2, 3)
    val n  = 5
    assert(g2(n) > g4(n))
  }

  test("grande valida parámetros con require") {
    assertThrows[IllegalArgumentException] { grande(0, 2) }
    assertThrows[IllegalArgumentException] { grande(1, 0) }
  }

  // ============================================================
  //               PRUEBAS (COMPLEMENTO, UNION, INTERSECCION)
  // ============================================================

  test("complemento: invierte 0 y 1 correctamente") {
    val cdCero: ConjDifuso = (_: Int) => 0.0
    val cdUno: ConjDifuso  = (_: Int) => 1.0
    val comp0 = complemento(cdCero)
    val comp1 = complemento(cdUno)
    assert(comp0(5) === 1.0)
    assert(comp1(5) === 0.0)
  }

  // ============================================================
  //                         UNIÓN
  // ============================================================

  test("union: devuelve el mayor grado de pertenencia") {
    val g1 = grande(2, 2)
    val g2 = grande(4, 2)
    (1 to 5).foreach { x =>
      val u = union(g1, g2)(x)
      assert(u === math.max(g1(x), g2(x)))
    }
  }

  // ============================================================
  //                       INTERSECCIÓN
  // ============================================================

  test("interseccion: devuelve el menor grado de pertenencia") {
    val g1 = grande(2, 2)
    val g2 = grande(4, 2)
    (1 to 5).foreach { x =>
      val i = interseccion(g1, g2)(x)
      assert(i === math.min(g1(x), g2(x)))
    }
  }

  // ============================================================
  //               PRUEBAS (INCLUSION E IGUALDAD)
  // ============================================================

  val cdCero: ConjDifuso = (_: Int) => 0.0              // conjunto vacío (todo 0)
  val cdUno: ConjDifuso = (_: Int) => 1.0               // conjunto total (todo 1)
  val cdMitad: ConjDifuso = (_: Int) => 0.5             // conjunto constante en 0.5
  val cdGrande: ConjDifuso = grande(1, 2)               // conjunto menos restrictivo
  val cdMasGrande: ConjDifuso = grande(1, 3)            // conjunto más restrictivo

  test("inclusion: conjunto vacío está incluido en cualquier conjunto") {
    assert(inclusion(cdCero, cdGrande))
    assert(inclusion(cdCero, cdUno))
  }

  test("inclusion: el más restrictivo (e mayor) está incluido en el menos restrictivo (e menor)") {
    assert(inclusion(cdMasGrande, cdGrande))  // ✓ verdadero
  }

  test("inclusion: el menos restrictivo (e menor) NO está incluido en el más restrictivo (e mayor)") {
    assert(!inclusion(cdGrande, cdMasGrande)) // ✓ verdadero
  }

}