package racoon.util

import racoon.operators._

package object implicits {

  implicit lazy val stringAlgebra: ToAlgebra[String] = new ToAlgebra[String] { self =>

    def const(a: Const): Algebra[String] = new Algebra[String] {
      val value: String = a.value
    }

    def value[T](a: Value[T]): Algebra[String] = new Algebra[String] {
      val value: String = a match {
        case Value(v: String) => s""""$v""""
        case Value(v) => s"${v.toString}"
      }
    }

    def values[T](a: Values[T]): Algebra[String] = new Algebra[String] {
      val value: String = a.values.map(_.value).toList.mkString("(", ",", ")")
    }

    def equal[T](left: Const, right: Value[T]): Algebra[String] = new Algebra[String] {
      val value: String = s"${const(left).value} = ${self.value(right).value}"
    }

    def notEqual[T](left: Const, right: Value[T]): Algebra[String] = new Algebra[String] {
      val value: String = s"${const(left).value} != ${self.value(right).value}"
    }

    def greaterThan[T](left: Const, right: Value[T]): Algebra[String] = new Algebra[String] {
      val value: String = s"${const(left).value} > ${self.value(right).value}"
    }

    def lowerThan[T](left: Const, right: Value[T]): Algebra[String] = new Algebra[String] {
      val value: String = s"${const(left).value} < ${self.value(right).value}"
    }

    def greaterThanEqual[T](left: Const, right: Value[T]): Algebra[String] = new Algebra[String] {
      val value: String = s"${const(left).value} >= ${self.value(right).value}"
    }

    def lowerThanEqual[T](left: Const, right: Value[T]): Algebra[String] = new Algebra[String] {
      val value: String = s"${const(left).value} <= ${self.value(right).value}"
    }

    def like(left: Const, right: Value[String]): Algebra[String] = new Algebra[String] {
      val value: String = s"${const(left).value} like ${self.value(right).value}"
    }

    def notLike(left: Const, right: Value[String]): Algebra[String] = new Algebra[String] {
      val value: String = s"${const(left).value} not like ${self.value(right).value}"
    }

    def exists(left: Const): Algebra[String] = new Algebra[String] {
      val value: String = s"${const(left).value} exists"
    }

    def notExists(left: Const): Algebra[String] = new Algebra[String] {
      val value: String = s"${const(left).value} not exists"
    }

    def in[T](left: Const, right: Values[T]): Algebra[String] = new Algebra[String] {
      val value: String = s"${const(left).value} in ${self.values(right).value}"
    }

    def notIn[T](left: Const, right: Values[T]): Algebra[String] = new Algebra[String] {
      val value: String = s"${const(left).value} not in ${self.values(right).value}"
    }

    def between[T](left: Const, from: Value[T], to: Value[T]): Algebra[String] = new Algebra[String] {
      val value: String = s"${const(left).value} between ${self.value(from).value} and ${self.value(to).value}"
    }

    def and(left: Algebra[String], right: Algebra[String]): Algebra[String] = new Algebra[String] {
      val value: String = s"(${left.value}) and (${right.value})"
    }

    def or(left: Algebra[String], right: Algebra[String]): Algebra[String] = new Algebra[String] {
      val value: String = s"(${left.value}) or (${right.value})"
    }
  }

}
