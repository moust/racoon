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

import cats.syntax.eq._
import racoon.operators._
import implicits._
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll

import scala.reflect._

class AlgebraSuite extends munit.ScalaCheckSuite {

  def testType[A: Arbitrary: ClassTag](): Unit =
    test(s"Value[${classTag[A]}]") {
      forAll { (x: A) =>
        assert(Value(x).value.toString === x.toString)
      }
    }

  testType[Byte]()
  testType[Short]()
  testType[Int]()
  testType[Long]()
  testType[BigDecimal]()
  testType[Float]()
  testType[Double]()
  testType[String]()

  test("""foo = "bar"""") {
    assertEquals(
      Eql(Const("foo"), Value("bar")).algebra[String].value,
      """foo = "bar""""
    )
  }

  test("""foo != "bar"""") {
    assertEquals(
      NotEql(Const("foo"), Value("bar")).algebra[String].value,
      """foo != "bar""""
    )
  }

  test("""foo > 0""") {
    assertEquals(
      Gt(Const("foo"), Value(0)).algebra[String].value,
      """foo > 0"""
    )
  }

  test("""foo < 100""") {
    assertEquals(
      Lt(Const("foo"), Value(100)).algebra[String].value,
      """foo < 100"""
    )
  }

  test("""foo >= 0""") {
    assertEquals(
      Gte(Const("foo"), Value(0)).algebra[String].value,
      """foo >= 0"""
    )
  }

  test("""foo <= 100""") {
    assertEquals(
      Lte(Const("foo"), Value(100)).algebra[String].value,
      """foo <= 100"""
    )
  }

  test("foo exists") {
    assertEquals(
      Exists(Const("foo")).algebra[String].value,
      "foo exists"
    )
  }

  test("""foo like "%bar%"""") {
    assertEquals(
      Like(Const("foo"), Value("%bar%")).algebra[String].value,
      """foo like "%bar%""""
    )
  }

  test("""foo not like "%bar%"""") {
    assertEquals(
      NotLike(Const("foo"), Value("%bar%")).algebra[String].value,
      """foo not like "%bar%""""
    )
  }

  test("foo not exists") {
    assertEquals(
      NotExists(Const("foo")).algebra[String].value,
      "foo not exists"
    )
  }

  test("x in (1,2,3)") {
    assertEquals(
      In(Const("x"), Values(List(1, 2, 3))).algebra[String].value,
      "x in (1,2,3)"
    )
  }

  test("x not in (1,2,3)") {
    assertEquals(
      NotIn(Const("x"), Values(List(1, 2, 3))).algebra[String].value,
      "x not in (1,2,3)"
    )
  }

  test("x between 1 and 10") {
    assertEquals(
      Between(Const("x"), Value(1), Value(10)).algebra[String].value,
      "x between 1 and 10"
    )
  }

  test("""(foo = "bar") or (foo != "baz")""") {
    assertEquals(
      Or(Eql(Const("foo"), Value("bar")), NotEql(Const("foo"), Value("baz"))).algebra[String].value,
      """(foo = "bar") or (foo != "baz")"""
    )
  }

  test("""(x = 5) and (((y = 2.1) or (z != -2)) or (p = "foo"))""") {
    assertEquals(
      And(
        Eql(Const("x"), Value(5)),
        Or(Or(Eql(Const("y"), Value(2.1)), NotEql(Const("z"), Value(-2))), Eql(Const("p"), Value("foo")))
      ).algebra[String].value,
      """(x = 5) and (((y = 2.1) or (z != -2)) or (p = "foo"))"""
    )
  }

}
