package racoon.util

import cats.data.NonEmptyList

object operators {

  sealed trait Op {

    def to[A: ToAlgebra]: A = algebra.value

    def algebra[A: ToAlgebra]: Algebra[A]

    /**
     * Apply `f` recursively to operators
     */
    def transform(f: Op => Op): Op = f(this)

  }

  case class Const(value: String) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] = {
      algebra.const(this)
    }
  }

  case class Value[T](value: T) extends Op {
    override def toString: String = s"Value[${value.getClass.getSimpleName}]($value)"
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.value(this)
  }

  case class Values[T](values: NonEmptyList[Value[T]]) extends Op {
    override def toString: String = s"Values(${values.toList.mkString(",")})"
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.values(this)
  }

  case class Eql[T](left: Const, right: Value[T]) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] = {
      algebra.equal(left, right)
    }
  }

  case class NotEql[T](left: Const, right: Value[T]) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.notEqual(left, right)
  }

  case class Gt[T](left: Const, right: Value[T]) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.greaterThan(left, right)
  }

  case class Lt[T](left: Const, right: Value[T]) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.lowerThan(left, right)
  }

  case class Gte[T](left: Const, right: Value[T]) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.greaterThanEqual(left, right)
  }

  case class Lte[T](left: Const, right: Value[T]) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.lowerThanEqual(left, right)
  }

  case class Like[T](left: Const, right: Value[String]) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.like(left, right)
  }

  case class NotLike[T](left: Const, right: Value[String]) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.notLike(left, right)
  }

  case class Exists(left: Const) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.exists(left)
  }

  case class NotExists(left: Const) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.notExists(left)
  }

  case class In[T](left: Const, right: Values[T]) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.in(left, right)
  }

  case class NotIn[T](left: Const, right: Values[T]) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.notIn(left, right)
  }

  case class Between[T](left: Const, from: Value[T], to: Value[T]) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.between(left, from, to)
  }

  case class And(left: Op, right: Op) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] =
      algebra.and(left.algebra[A], right.algebra[A])

    override def transform(f: Op => Op): Op = And(left.transform(f), right.transform(f))
  }

  case class Or(left: Op, right: Op) extends Op {
    def algebra[A](implicit algebra: ToAlgebra[A]): Algebra[A] = {
      algebra.or(left.algebra[A], right.algebra[A])
    }

    override def transform(f: Op => Op): Op = Or(left.transform(f), right.transform(f))
  }

}