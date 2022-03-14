package racoon.util

import racoon.operators._

class OperatorsSuite extends munit.FunSuite {

  test("transform Eql operator to Like") {
    val operation = Eql(Const("foo"), Value("bar"))
    val expected = Like(Const("foo"), Value("%bar%"))
    assertEquals(
      operation.transform {
        case Eql(const @ Const("foo"), Value(value: String)) => Like(const, Value(s"%$value%"))
        case op => op
      },
      expected
    )
  }

  test("transform Eql operator's const and value") {
    val operation = Eql(Const("foo"), Value("bar"))
    val expected = Eql(Const("oof"), Value("rab"))
    assertEquals(
      operation.transform {
        case Eql(Const(const), Value(value: String)) => Eql(Const(const.reverse), Value(value.reverse))
        case op => op
      },
      expected
    )
  }

  test("transform complexe operator") {
    val operation = And(
      Eql(Const("username"), Value("bob@mail.com")),
      Eql(Const("password"), Value("******"))
    )
    val expected = And(
      Or(
        Eql(Const("username"), Value("bob@mail.com")),
        Eql(Const("email"), Value("bob@mail.com")),
      ),
      Eql(Const("password"), Value("******"))
    )
    assertEquals(
      operation.transform {
        case Eql(Const("username"), value) =>
          Or(
            Eql(Const("username"), value),
            Eql(Const("email"), value),
          )
        case op => op
      },
      expected
    )
  }

}
