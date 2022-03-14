package racoon.util

import racoon.operators._

trait Algebra[A] {
  val value: A
}

trait ToAlgebra[A] {

  def const(a: Const): Algebra[A]

  def value[T](a: Value[T]): Algebra[A]

  def values[T](a: Values[T]): Algebra[A]

  def equal[T](left: Const, right: Value[T]): Algebra[A]

  def notEqual[T](left: Const, right: Value[T]): Algebra[A]

  def greaterThan[T](left: Const, right: Value[T]): Algebra[A]

  def lowerThan[T](left: Const, right: Value[T]): Algebra[A]

  def greaterThanEqual[T](left: Const, right: Value[T]): Algebra[A]

  def lowerThanEqual[T](left: Const, right: Value[T]): Algebra[A]

  def like(left: Const, right: Value[String]): Algebra[A]

  def notLike(left: Const, right: Value[String]): Algebra[A]

  def exists(left: Const): Algebra[A]

  def notExists(left: Const): Algebra[A]

  def in[T](left: Const, right: Values[T]): Algebra[A]

  def notIn[T](left: Const, right: Values[T]): Algebra[A]

  def between[T](left: Const, from: Value[T], to: Value[T]): Algebra[A]

  def and(left: Algebra[A], right: Algebra[A]): Algebra[A]

  def or(left: Algebra[A], right: Algebra[A]): Algebra[A]

}
