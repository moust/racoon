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
