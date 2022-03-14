package racoon.doobie

import cats.data.NonEmptyList
import doobie.{Fragment, Fragments}
import doobie.implicits._
import racoon.doobie.implicits._
import racoon.operators._

class DoobieAlgebraSuite extends munit.FunSuite {

  test("""foo = "bar"""") {
    val operator = Eql(Const("foo"), Value("bar"))
    val expected = fr"foo = ${"bar"}"
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo != "bar"""") {
    val operator = NotEql(Const("foo"), Value("bar"))
    val expected = fr"foo != ${"bar"}"
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo > 0""") {
    val operator = Gt(Const("foo"), Value(0))
    val expected = fr"foo > ${0}"
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo < 100""") {
    val operator = Lt(Const("foo"), Value(100))
    val expected = fr"foo < ${100}"
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo >= 0""") {
    val operator = Gte(Const("foo"), Value(0))
    val expected = fr"foo >= ${0}"
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo <= 100""") {
    val operator = Lte(Const("foo"), Value(100))
    val expected = fr"foo <= ${100}"
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo LIKE "%bar%"""") {
    val operator = Like(Const("foo"), Value("%bar%"))
    val expected = fr"foo LIKE ${"%bar%"}"
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo NOT LIKE "%bar%"""") {
    val operator = NotLike(Const("foo"), Value("%bar%"))
    val expected = fr"foo NOT LIKE ${"%bar%"}"
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo EXISTS""") {
    val operator = Exists(Const("foo"))
    val expected = fr"foo IS NOT NULL"
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo NOT EXISTS""") {
    val operator = NotExists(Const("foo"))
    val expected = fr"foo IS NULL"
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo IN [1, 2, 3]""") {
    val operator = In(Const("foo"), Values(NonEmptyList.of(Value(1), Value(2), Value(3))))
    val expected = Fragments.in(fr"foo", NonEmptyList.of(1, 2, 3))
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo NOT IN [1, 2, 3]""") {
    val operator = NotIn(Const("foo"), Values(NonEmptyList.of(Value(1), Value(2), Value(3))))
    val expected = Fragments.notIn(fr"foo", NonEmptyList.of(1, 2, 3))
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo BETWEEN 1 AND 10""") {
    val operator = Between(Const("foo"), Value(1), Value(10))
    val expected = fr"foo BETWEEN ${1} AND ${10}"
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""(foo = "bar") AND (baz = "bar")""") {
    val operator = And(Eql(Const("foo"), Value("bar")), Eql(Const("baz"), Value("bar")))
    val expected = fr"(foo = ${"bar"}) AND (baz = ${"bar"})"
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""(foo = "bar") OR (baz = "bar")""") {
    val operator = Or(Eql(Const("foo"), Value("bar")), Eql(Const("baz"), Value("bar")))
    val expected = fr"(foo = ${"bar"}) OR (baz = ${"bar"})"
    val result = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

}
