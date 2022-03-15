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

import racoon.operators._

class OperatorsSuite extends munit.FunSuite {

  test("transform Eql operator to Like") {
    val operation = Eql(Const("foo"), Value("bar"))
    val expected  = Like(Const("foo"), Value("%bar%"))
    assertEquals(
      operation.transform {
        case Eql(const @ Const("foo"), Value(value: String)) => Like(const, Value(s"%$value%"))
        case op                                              => op
      },
      expected
    )
  }

  test("transform Eql operator's const and value") {
    val operation = Eql(Const("foo"), Value("bar"))
    val expected  = Eql(Const("oof"), Value("rab"))
    assertEquals(
      operation.transform {
        case Eql(Const(const), Value(value: String)) => Eql(Const(const.reverse), Value(value.reverse))
        case op                                      => op
      },
      expected
    )
  }

  test("transform complex operator") {
    val operation = And(
      Eql(Const("username"), Value("bob@mail.com")),
      Eql(Const("password"), Value("******"))
    )
    val expected = And(
      Or(
        Eql(Const("username"), Value("bob@mail.com")),
        Eql(Const("email"), Value("bob@mail.com"))
      ),
      Eql(Const("password"), Value("******"))
    )
    assertEquals(
      operation.transform {
        case Eql(Const("username"), value) =>
          Or(
            Eql(Const("username"), value),
            Eql(Const("email"), value)
          )
        case op => op
      },
      expected
    )
  }

}
