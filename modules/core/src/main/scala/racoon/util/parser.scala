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

import atto.Atto._
import atto.Parser
import cats.data.NonEmptyList
import operators._

object parser {

  /**
   * Parse query argument, yielding a corresponding `[[Op]]` sequence.
   */
  def parse(query: String): Either[String, Op] = Expr.parseOnly(query).either

  private def operator(t: String): Parser[String] = token(many(whitespace) ~> stringCI(t) <~ many(whitespace))

  private lazy val number: Parser[Any] = bigDecimal.map {
    // case n if n.isValidByte => n.toByte
    // case n if n.isValidShort => n.toShort
    case n if n.isValidInt => n.toInt
    case n if n.isValidLong => n.toLong
    case n if n.isDecimalFloat => n.toFloat
    case n if n.isDecimalDouble => n.toDouble
    case n => n
  }

  private lazy val boolean: Parser[Boolean] = stringCI("true").map(_ => true) | stringCI("false").map(_ => false)

  private lazy val const: Parser[String] =
    stringOf1(letterOrDigit | char('-') | char('_') | char('.'))

  private lazy val value: Parser[Any] = number | boolean | stringLiteral

  private lazy val values: Parser[List[Any]] = parens(sepBy(value, token(many(whitespace) ~> char(',') <~ many(whitespace))))

  private lazy val Expr: Parser[Op] = for {
    lhs <- Factor
    next <- Expr_prim
  } yield next(lhs)

  private lazy val Expr_prim: Parser[Op => Op] = {
    val p1 = for {
      _ <- operator("and")
      rhs <- Factor
      next <- Expr_prim
    } yield (lhs: Op) => next(And(lhs, rhs))
    val p2 = for {
      _ <- operator("or")
      rhs <- Factor
      next <- Expr_prim
    } yield (lhs: Op) => next(Or(lhs, rhs))
    val p3 = ok((lhs: Op) => lhs)
    p1 | p2 | p3
  }

  private lazy val Term: Parser[Op] = for {
    lhs <- const
    next <- Term_prim
  } yield next(Const(lhs))

  private def op(ts: String*): Parser[Value[Any]] = for {
    _ <- ts.map(operator).reduce(_ | _)
    rhs <- value
  } yield Value(rhs)

  private def like: Parser[Const => Op] = for {
    not <- opt(operator("not"))
    _ <- operator("like")
    rhs <- stringLiteral
  } yield not match {
    case Some(_) => (lhs: Const) => NotLike(lhs, Value(rhs))
    case None => (lhs: Const) => Like(lhs, Value(rhs))
  }

  private def exists: Parser[Const => Op] = for {
    not <- opt(operator("not"))
    _ <- operator("exists")
  } yield not match {
    case Some(_) => (lhs: Const) => NotExists(lhs)
    case None => (lhs: Const) => Exists(lhs)
  }

  private def in: Parser[Const => Op] = {
    for {
      not <- opt(operator("not"))
      _ <- operator("in")
      rhs <- values
    } yield {
      val values = Values(NonEmptyList.fromListUnsafe(rhs.map(Value(_))))
      not match {
        case Some(_) => (lhs: Const) => NotIn(lhs, values)
        case None => (lhs: Const) => In(lhs, values)
      }
    }
  }

  private def between: Parser[Const => Op] = for {
    _ <- operator("between")
    rhs1 <- value
    _ <- operator("and")
    rhs2 <- value
  } yield (lhs: Const) => Between(lhs, Value(rhs1), Value(rhs2))

  private lazy val Term_prim: Parser[Const => Op] = {
    op("==", "=").map(rhs => (lhs: Const) => Eql(lhs, rhs)) |
    op("!=", "<>").map(rhs => (lhs: Const) => NotEql(lhs, rhs)) |
    op(">").map(rhs => (lhs: Const) => Gt(lhs, rhs)) |
    op("<").map(rhs => (lhs: Const) => Lt(lhs, rhs)) |
    op(">=").map(rhs => (lhs: Const) => Gte(lhs, rhs)) |
    op("<=").map(rhs => (lhs: Const) => Lte(lhs, rhs)) |
    like |
    exists |
    in |
    between |
    ok((lhs: Const) => lhs)
  }

  private lazy val Factor: Parser[Op] = {
    val p1 = for {
      _ <- char('(')
      e <- Expr
      _ <- char(')')
    } yield e
    val p2 = Term
    p1 | p2
  }

}
