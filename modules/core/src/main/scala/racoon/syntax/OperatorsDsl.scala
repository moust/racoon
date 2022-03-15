package racoon.syntax

import racoon.operators._

trait OperatorsDsl {

  final case class BetweenOps[T](const: Const, from: Value[T]) {
    def and(to: T): Between[T] = Between(const, from, Value(to))
  }

  implicit class StringOps(const: String) {
    def ===[T](value: T): Op = Eql(Const(const), Value(value))
    def =!=[T](value: T): Op = NotEql(Const(const), Value(value))
    def gt[T](value: T): Op = Gt(Const(const), Value(value))
    def lt[T](value: T): Op = Lt(Const(const), Value(value))
    def gte[T](value: T): Op = Gte(Const(const), Value(value))
    def lte[T](value: T): Op = Lte(Const(const), Value(value))
    def like(value: String): Op = Like(Const(const), Value(value))
    def notLike(value: String): Op = NotLike(Const(const), Value(value))
    def notNull: Op = Exists(Const(const))
    def isNull: Op = NotExists(Const(const))
    def in[T](values: List[T]): Op = In(Const(const), Values(values))
    def notIn[T](values: List[T]): Op = NotIn(Const(const), Values(values))
    def between[T](from: T): BetweenOps[T] = BetweenOps(Const(const), Value(from))
  }

  implicit class OperatorOps(op1: Op) {
    def and(op2: Op): Op = And(op1, op2)
    def or(op2: Op): Op = Or(op1, op2)
  }

}

object OperatorsDsl extends OperatorsDsl
