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

import java.time.{LocalDate, LocalDateTime, LocalTime, ZonedDateTime}

import doobie.{Fragment, Fragments}
import doobie.implicits._
import doobie.implicits.javatimedrivernative._
import racoon.{Algebra, ToAlgebra}
import racoon.operators._

trait algebra {

  implicit val doobieAlgebra: ToAlgebra[Fragment] = new ToAlgebra[Fragment] { self =>
    implicit class ConstOps(a: Const) {
      def toFragment: Fragment = const(a).value
    }

    implicit class ValueOps[T](a: Value[T]) {
      def toFragment: Fragment = value(a).value
    }

    implicit class ValuesOps[T](a: Values[T]) {
      def toFragment: Fragment = values(a).value
    }

    override def const(a: Const): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = Fragment.const0(a.value)
    }

    override def value[T](a: Value[T]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = a match {
        case Value(v: String)        => Fragments.values[String](v)
        case Value(v: Long)          => Fragments.values[Long](v)
        case Value(v: Int)           => Fragments.values[Int](v)
        case Value(v: Short)         => Fragments.values[Short](v)
        case Value(v: Byte)          => Fragments.values[Byte](v)
        case Value(v: BigDecimal)    => Fragments.values[BigDecimal](v)
        case Value(v: Double)        => Fragments.values[Double](v)
        case Value(v: Float)         => Fragments.values[Float](v)
        case Value(v: Boolean)       => Fragments.values[Boolean](v)
        case Value(v: LocalTime)     => Fragments.values[LocalTime](v)
        case Value(v: LocalDate)     => Fragments.values[LocalDate](v)
        case Value(v: LocalDateTime) => Fragments.values[LocalDateTime](v)
        case Value(v: ZonedDateTime) => Fragments.values[ZonedDateTime](v)
        case _ => throw new UnsupportedOperationException(s"Value $a of type ${a.getClass} is not supported.")
      }
    }

    override def values[T](a: Values[T]): Algebra[Fragment] = new Algebra[Fragment] {
      // val value: Fragment = a.map(_.value).foldSmash1(fr0"", fr",", fr0"")
      val value: Fragment = a.values.map(self.value(_).value).foldSmash1(fr0"", fr",", fr0"")
    }

    override def equal[T](left: Const, right: Value[T]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = fr0"${left.toFragment} = ${right.toFragment}"
    }

    override def notEqual[T](left: Const, right: Value[T]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = fr0"${left.toFragment} != ${right.toFragment}"
    }

    override def greaterThan[T](left: Const, right: Value[T]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = fr0"${left.toFragment} > ${right.toFragment}"
    }

    override def lowerThan[T](left: Const, right: Value[T]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = fr0"${left.toFragment} < ${right.toFragment}"
    }

    override def greaterThanEqual[T](left: Const, right: Value[T]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = fr0"${left.toFragment} >= ${right.toFragment}"
    }

    override def lowerThanEqual[T](left: Const, right: Value[T]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = fr0"${left.toFragment} <= ${right.toFragment}"
    }

    override def like(left: Const, right: Value[String]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = fr0"${left.toFragment} LIKE ${right.toFragment}"
    }

    override def notLike(left: Const, right: Value[String]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = fr0"${left.toFragment} NOT LIKE ${right.toFragment}"
    }

    override def exists(left: Const): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = fr0"${left.toFragment} IS NOT NULL"
    }

    override def notExists(left: Const): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = fr0"${left.toFragment} IS NULL"
    }

    override def in[T](left: Const, right: Values[T]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = fr0"${left.toFragment} IN (${right.toFragment})"
    }

    override def notIn[T](left: Const, right: Values[T]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = fr0"${left.toFragment} NOT IN (${right.toFragment})"
    }

    override def between[T](left: Const, from: Value[T], to: Value[T]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = fr0"${left.toFragment} BETWEEN ${from.toFragment} AND ${to.toFragment}"
    }

    override def and(left: Algebra[Fragment], right: Algebra[Fragment]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = Fragments.and(left.value, right.value)
    }

    override def or(left: Algebra[Fragment], right: Algebra[Fragment]): Algebra[Fragment] = new Algebra[Fragment] {
      val value: Fragment = Fragments.or(left.value, right.value)
    }
  }

}
