/*
 * Copyright 2022 Quentin Aupetit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package racoon.util

import cats.data.NonEmptyList
import cats.syntax.eq._
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Prop.forAll
import racoon.operators._

class ParserSuite extends munit.ScalaCheckSuite {

  def testType[A: Arbitrary](t: String): Unit =
    test(s"Parse $t") {
      forAll { (x: A) =>
        assert(Value(x).value.toString === x.toString)
      }
    }

  testType[Byte]("Byte")
  testType[Short]("Short")
  testType[Int]("Int")
  testType[Long]("Long")
  testType[BigDecimal]("BigDecimal")
  testType[Float]("Float")
  testType[Double]("Double")
  testType[String]("String")

  test("Parser must parse x=1") {
    assertEquals(
      parser.parse("x=1"),
      Right(Eql(Const("x"), Value(1)))
    )
  }

  test("Parser must parse x = 123") {
    assertEquals(
      parser.parse("x = 123"),
      Right(Eql(Const("x"), Value(123)))
    )
  }

  test("Parser must parse x == 123") {
    assertEquals(
      parser.parse("x == 123"),
      Right(Eql(Const("x"), Value(123)))
    )
  }

  test("Parser must parse x = -123") {
    assertEquals(
      parser.parse("x = 123"),
      Right(Eql(Const("x"), Value(123)))
    )
  }

  test("Parser must parse user.id = 123") {
    assertEquals(
      parser.parse("user.id = 123"),
      Right(Eql(Const("user.id"), Value(123)))
    )
  }

  test("Parser must parse user_id = 123") {
    assertEquals(
      parser.parse("user_id = 123"),
      Right(Eql(Const("user_id"), Value(123)))
    )
  }

  test("Parser must parse x = 1.23") {
    assertEquals(
      parser.parse("x = 1.23"),
      Right(Eql(Const("x"), Value(1.23.toFloat)))
    )
  }

  test(s"Parser must parse x = ${Float.MaxValue}") {
    assertEquals(
      parser.parse(s"x = ${Float.MaxValue}"),
      Right(Eql(Const("x"), Value(Float.MaxValue)))
    )
  }

  test(s"Parser must parse x = ${Double.MaxValue}") {
    assertEquals(
      parser.parse(s"x = ${Double.MaxValue}"),
      Right(Eql(Const("x"), Value(Double.MaxValue)))
    )
  }

  test("Parser must parse x = true") {
    assertEquals(
      parser.parse("x = true"),
      Right(Eql(Const("x"), Value(true)))
    )
  }

  test("Parser must parse x = false") {
    assertEquals(
      parser.parse("x = false"),
      Right(Eql(Const("x"), Value(false)))
    )
  }

  test("""Parser must parse x = "foo" """) {
    assertEquals(
      parser.parse("""x = "foo" """),
      Right(Eql(Const("x"), Value("foo")))
    )
  }

  test("""Parser must parse x = "foo-bar_baz.bat" """) {
    assertEquals(
      parser.parse("""x = "foo-bar_baz.bat" """),
      Right(Eql(Const("x"), Value("foo-bar_baz.bat")))
    )
  }

  test("Parser must parse x != 123") {
    assertEquals(
      parser.parse("x != 123"),
      Right(NotEql(Const("x"), Value(123)))
    )
  }

  test("Parser must parse x <> 123") {
    assertEquals(
      parser.parse("x <> 123"),
      Right(NotEql(Const("x"), Value(123)))
    )
  }

  test("Parser must parse user.id exists") {
    assertEquals(
      parser.parse("user.id exists"),
      Right(Exists(Const("user.id")))
    )
  }

  test("Parser must parse user.id not exists") {
    assertEquals(
      parser.parse("user.id not exists"),
      Right(NotExists(Const("user.id")))
    )
  }

  test("""Parser must parse x like "%foo%" """) {
    assertEquals(
      parser.parse("""x like "%foo%" """),
      Right(Like(Const("x"), Value("%foo%")))
    )
  }

  test("""Parser must parse x not like "%foo%" """) {
    assertEquals(
      parser.parse("""x not like "%foo%" """),
      Right(NotLike(Const("x"), Value("%foo%")))
    )
  }

  test("Parser must parse x in (1, 2, 3)") {
    assertEquals(
      parser.parse("x IN (1, 2, 3)"),
      Right(In(Const("x"), Values(NonEmptyList.of(Value(1), Value(2), Value(3)))))
    )
  }

  test("Parser must parse x not in (1, 2, 3)") {
    assertEquals(
      parser.parse("x NOT IN (1, 2, 3)"),
      Right(NotIn(Const("x"), Values(NonEmptyList.of(Value(1), Value(2), Value(3)))))
    )
  }

  test("Parser must parse x = 1 and y = 2") {
    assertEquals(
      parser.parse("x = 1 and y = 2"),
      Right(And(Eql(Const("x"), Value(1)), Eql(Const("y"), Value(2))))
    )
  }

  test("Parser must parse x = 1 or y = 2") {
    assertEquals(
      parser.parse("x = 1 or y = 2"),
      Right(Or(Eql(Const("x"), Value(1)), Eql(Const("y"), Value(2))))
    )
  }

  test("Parser must parse x = 1 and (y = 2 or z = 3)") {
    assertEquals(
      parser.parse("x = 1 and (y = 2 or z = 3)"),
      Right(And(Eql(Const("x"), Value(1)), Or(Eql(Const("y"), Value(2)), Eql(Const("z"), Value(3)))))
    )
  }

  test("""(ip = "127.0.0.1" or identifier = "John Do") and account = "local-sandbox"""") {
    assertEquals(
      parser.parse("""(ip = "127.0.0.1" or identifier = "John Do") and account = "local-sandbox""""),
      Right(
        And(
          Or(
            Eql(Const("ip"), Value("127.0.0.1")),
            Eql(Const("identifier"), Value("John Do"))
          ),
          Eql(Const("account"), Value("local-sandbox"))
        )
      )
    )
  }

}
