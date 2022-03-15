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

package racoon.doobie

import cats.data.NonEmptyList
import doobie.{Fragment, Fragments}
import doobie.implicits._
import racoon.doobie.implicits._
import racoon.implicits._

import scala.language.postfixOps

class DoobieAlgebraSuite extends munit.FunSuite {

  test("""foo = "bar"""") {
    val operator = "foo" === "bar"
    val expected = fr"foo = ${"bar"}"
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo != "bar"""") {
    val operator = "foo" =!= "bar"
    val expected = fr"foo != ${"bar"}"
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo > 0""") {
    val operator = "foo" gt 0
    val expected = fr"foo > ${0}"
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo < 100""") {
    val operator = "foo" lt 100
    val expected = fr"foo < ${100}"
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo >= 0""") {
    val operator = "foo" gte 0
    val expected = fr"foo >= ${0}"
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo <= 100""") {
    val operator = "foo" lte 100
    val expected = fr"foo <= ${100}"
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo LIKE "%ba?"""") {
    val operator = "foo" like "%ba?"
    val expected = fr"foo LIKE ${"%ba?"}"
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo NOT LIKE "%ba?"""") {
    val operator = "foo" notLike "%ba?"
    val expected = fr"foo NOT LIKE ${"%ba?"}"
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo EXISTS""") {
    val operator = "foo" notNull
    val expected = fr"foo IS NOT NULL"
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo NOT EXISTS""") {
    val operator = "foo" isNull
    val expected = fr"foo IS NULL"
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo IN [1, 2, 3]""") {
    val operator = "foo" in List(1, 2, 3)
    val expected = Fragments.in(fr"foo", NonEmptyList.of(1, 2, 3))
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo NOT IN [1, 2, 3]""") {
    val operator = "foo" notIn List(1, 2, 3)
    val expected = Fragments.notIn(fr"foo", NonEmptyList.of(1, 2, 3))
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""foo BETWEEN 1 AND 10""") {
    val operator = "foo" between 1 and 10
    val expected = fr"foo BETWEEN ${1} AND ${10}"
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""(foo = "bar") AND (baz = "bar")""") {
    val operator = "foo" === "bar" and "baz" === "bar"
    val expected = fr"(foo = ${"bar"}) AND (baz = ${"bar"})"
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

  test("""(foo = "bar") OR (baz = "bar")""") {
    val operator = "foo" === "bar" or "baz" === "bar"
    val expected = fr"(foo = ${"bar"}) OR (baz = ${"bar"})"
    val result   = operator.to[Fragment]
    assertNoDiff(result.internals.sql, expected.internals.sql)
    assertEquals(result.internals.elements, expected.internals.elements)
  }

}
