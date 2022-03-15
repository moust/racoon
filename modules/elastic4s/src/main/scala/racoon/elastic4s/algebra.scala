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

package racoon.elastic4s

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.requests.searches.queries.Query
import racoon.util.{Algebra, ToAlgebra}
import racoon.operators._
import racoon.elastic4s.implicits._

trait algebra {

  search("")

  implicit val elastic4sAlgebra: ToAlgebra[Query] = new ToAlgebra[Query] { self =>
    def const(a: Const): Algebra[Query] = throw new UnsupportedOperationException

    def value[T](a: Value[T]): Algebra[Query] = throw new UnsupportedOperationException

    def values[T](a: Values[T]): Algebra[Query] = throw new UnsupportedOperationException

    override def equal[T](left: Const, right: Value[T]): Algebra[Query] = new Algebra[Query] {
      val value: Query = termQuery(left.value, right.value)
    }

    override def notEqual[T](left: Const, right: Value[T]): Algebra[Query] = new Algebra[Query] {
      val value: Query = not(equal(left, right).value)
    }

    override def greaterThan[T](left: Const, right: Value[T]): Algebra[Query] = new Algebra[Query] {
      val value: Query = rangeQuery(left.value).gt0(right.value)
    }

    override def lowerThan[T](left: Const, right: Value[T]): Algebra[Query] = new Algebra[Query] {
      val value: Query = rangeQuery(left.value).lt0(right.value)
    }

    override def greaterThanEqual[T](left: Const, right: Value[T]): Algebra[Query] = new Algebra[Query] {
      val value: Query = rangeQuery(left.value).gte0(right.value)
    }

    override def lowerThanEqual[T](left: Const, right: Value[T]): Algebra[Query] = new Algebra[Query] {
      val value: Query = rangeQuery(left.value).lte0(right.value)
    }

    override def like(left: Const, right: Value[String]): Algebra[Query] = new Algebra[Query] {
      val value: Query = wildcardQuery(left.value, fromSqlWildcard(right.value))
    }

    override def notLike(left: Const, right: Value[String]): Algebra[Query] = new Algebra[Query] {
      val value: Query = not(wildcardQuery(left.value, fromSqlWildcard(right.value)))
    }

    def fromSqlWildcard(string: String): String =
      string.replaceAll("%", "*").replaceAll("_", "?")

    override def exists(left: Const): Algebra[Query] = new Algebra[Query] {
      val value: Query = existsQuery(left.value)
    }

    override def notExists(left: Const): Algebra[Query] = new Algebra[Query] {
      val value: Query = not(existsQuery(left.value))
    }

    override def in[T](left: Const, right: Values[T]): Algebra[Query] = new Algebra[Query] {
      val value: Query = termsQuery(left.value, right.values.map(_.value))
    }

    override def notIn[T](left: Const, right: Values[T]): Algebra[Query] = new Algebra[Query] {
      val value: Query = not(termsQuery(left.value, right.values.map(_.value)))
    }

    override def between[T](left: Const, from: Value[T], to: Value[T]): Algebra[Query] = new Algebra[Query] {
      val value: Query = rangeQuery(left.value).gte0(from.value).lte0(to.value)
    }

    override def and(left: Algebra[Query], right: Algebra[Query]): Algebra[Query] = new Algebra[Query] {
      val value: Query = boolQuery().must(left.value, right.value)
    }

    override def or(left: Algebra[Query], right: Algebra[Query]): Algebra[Query] = new Algebra[Query] {
      val value: Query = boolQuery().should(left.value, right.value)
    }

  }

}
