package racoon.util

import racoon.operators._
import racoon.implicits._

import scala.language.postfixOps

class SyntaxSuite extends munit.FunSuite {

  test("Eql") {
    assertEquals(
      "foo" === "bar",
      Eql(Const("foo"), Value("bar"))
    )
    assertEquals(
      "foo" === 1,
      Eql(Const("foo"), Value(1))
    )
    assertEquals(
      "foo" === 1.2,
      Eql(Const("foo"), Value(1.2))
    )
  }

  test("NotEql") {
    assertEquals(
      "foo" =!= "bar",
      NotEql(Const("foo"), Value("bar"))
    )
    assertEquals(
      "foo" =!= 1,
      NotEql(Const("foo"), Value(1))
    )
    assertEquals(
      "foo" =!= 1.2,
      NotEql(Const("foo"), Value(1.2))
    )
  }

  test("Gt") {
    assertEquals(
      "foo" gt "bar",
      Gt(Const("foo"), Value("bar"))
    )
    assertEquals(
      "foo" gt 1,
      Gt(Const("foo"), Value(1))
    )
    assertEquals(
      "foo" gt 1.2,
      Gt(Const("foo"), Value(1.2))
    )
  }

  test("Lt") {
    assertEquals(
      "foo" lt "bar",
      Lt(Const("foo"), Value("bar"))
    )
    assertEquals(
      "foo" lt 1,
      Lt(Const("foo"), Value(1))
    )
    assertEquals(
      "foo" lt 1.2,
      Lt(Const("foo"), Value(1.2))
    )
  }

  test("Gte") {
    assertEquals(
      "foo" gte "bar",
      Gte(Const("foo"), Value("bar"))
    )
    assertEquals(
      "foo" gte 1,
      Gte(Const("foo"), Value(1))
    )
    assertEquals(
      "foo" gte 1.2,
      Gte(Const("foo"), Value(1.2))
    )
  }

  test("Lte") {
    assertEquals(
      "foo" lte "bar",
      Lte(Const("foo"), Value("bar"))
    )
    assertEquals(
      "foo" lte 1,
      Lte(Const("foo"), Value(1))
    )
    assertEquals(
      "foo" lte 1.2,
      Lte(Const("foo"), Value(1.2))
    )
  }

  test("Like") {
    assertEquals(
      "foo" like "%bar%",
      Like(Const("foo"), Value("%bar%"))
    )
  }

  test("NotLike") {
    assertEquals(
      "foo" notLike "%bar%",
      NotLike(Const("foo"), Value("%bar%"))
    )
  }

  test("Exists") {
    assertEquals(
      "x" notNull,
      Exists(Const("x"))
    )
  }

  test("NotExists") {
    assertEquals(
      "x" isNull,
      NotExists(Const("x"))
    )
  }

  test("In") {
    assertEquals(
      "x" in List(1, 2, 3),
      In(Const("x"), Values(List(1, 2, 3)))
    )
  }

  test("NotIn") {
    assertEquals(
      "x" notIn List(1, 2, 3),
      NotIn(Const("x"), Values(List(1, 2, 3)))
    )
  }

  test("Between") {
    assertEquals(
      "x" between 1 and 10,
      Between(Const("x"), Value(1), Value(10))
    )
  }

  test("And") {
    assertEquals(
      ("x" === 1) and ("y" === 2),
      And(
        Eql(Const("x"), Value(1)),
        Eql(Const("y"), Value(2))
      )
    )
  }

  test("Or") {
    assertEquals(
      ("x" === 1) or ("y" === 2),
      Or(
        Eql(Const("x"), Value(1)),
        Eql(Const("y"), Value(2))
      )
    )
  }

  test(""""user.id" === 123 and "user.status" === "active"""") {
    assertEquals(
      "user.id" === 123 and "user.status" === "active",
      And(
        Eql(Const("user.id"), Value(123)),
        Eql(Const("user.status"), Value("active"))
      )
    )
  }

  test("""("user.status" === "active") and (("user.team" === "developer") or ("user.role" in List("admin", "moderator")))""") {
    assertEquals(
      ("user.status" === "active") and (("user.team" === "developer") or ("user.role" in List("admin", "moderator"))),
      And(
        Eql(Const("user.status"), Value("active")),
        Or(
          Eql(Const("user.team"), Value("developer")),
          In(Const("user.role"), Values(List("admin", "moderator")))
        )
      )
    )
  }

  test("""user.status" === "active" and "user.team" === "developer" or ("user.role" in List("admin", "moderator"))""") {
    assertEquals(
      "user.status" === "active" and "user.team" === "developer" or ("user.role" in List("admin", "moderator")),
      Or(
        And(
          Eql(Const("user.status"), Value("active")),
          Eql(Const("user.team"), Value("developer"))
        ),
        In(Const("user.role"), Values(List("admin", "moderator")))
      )
    )
  }

}
