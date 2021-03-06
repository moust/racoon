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

import java.time.{LocalDate, LocalDateTime, LocalTime, ZonedDateTime}
import java.time.format.DateTimeFormatter

import cats.data.NonEmptyList
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.requests.searches.queries.Query
import racoon.elastic4s.implicits._
import racoon.implicits._

class Elastic4sAlgebraSuite extends munit.FunSuite {

  test("""foo = "bar"""") {
    val operator = "foo" === "bar"
    val expected = termQuery("foo", "bar")
    val result   = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""date = 13:37:00""") {
    val timeValue = LocalTime.parse("13:37:00")
    val operator  = "date" === timeValue
    val expected  = termQuery("date", timeValue)
    val result    = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""date = 2020-01-01""") {
    val dateValue = LocalDate.parse("2020-01-01")
    val operator  = "date" === dateValue
    val expected  = termQuery("date", dateValue)
    val result    = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""datetime = 2020-01-01T13:37:00""") {
    val dateTimeValue = LocalDateTime.parse("2020-01-01T13:37:00")
    val operator      = "date" === dateTimeValue
    val expected      = termQuery("date", dateTimeValue)
    val result        = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""datetime = 2020-01-01T13:37:00+01:00""") {
    val dateTimeValue = ZonedDateTime.parse("2020-01-01T13:37:00+01:00")
    val operator      = "date" === dateTimeValue
    val expected      = termQuery("date", dateTimeValue)
    val result        = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo != "bar"""") {
    val operator = "foo" =!= "bar"
    val expected = not(termQuery("foo", "bar"))
    val result   = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo > 0""") {
    val operator = "foo" gt 0
    val expected = rangeQuery("foo").gt(0)
    val result   = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""time > 13:37:00""") {
    val timeValue = LocalTime.parse("13:37:00")
    val operator  = "time" gt timeValue
    val expected  = rangeQuery("time").gt(timeValue.format(DateTimeFormatter.ISO_LOCAL_TIME))
    val result    = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""date > 2020-01-01""") {
    val dateValue = LocalDate.parse("2020-01-01")
    val operator  = "date" gt dateValue
    val expected  = rangeQuery("date").gt(dateValue.format(DateTimeFormatter.ISO_LOCAL_DATE))
    val result    = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""date > 2020-01-01T13:37:00""") {
    val dateTimeValue = LocalDateTime.parse("2020-01-01T13:37:00")
    val operator      = "date" gt dateTimeValue
    val expected      = rangeQuery("date").gt(dateTimeValue.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    val result        = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""datetime > 2020-01-01T13:37:00+01:00""") {
    val dateTimeValue = ZonedDateTime.parse("2020-01-01T13:37:00+01:00")
    val operator      = "date" gt dateTimeValue
    val expected      = rangeQuery("date").gt(dateTimeValue.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
    val result        = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo < 100""") {
    val operator = "foo" lt 100
    val expected = rangeQuery("foo").lt(100)
    val result   = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo >= 0""") {
    val operator = "foo" gte 0
    val expected = rangeQuery("foo").gte(0)
    val result   = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo <= 100""") {
    val operator = "foo" lte 100
    val expected = rangeQuery("foo").lte(100)
    val result   = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo LIKE "%ba_"""") {
    val operator = "foo" like "%ba_"
    val expected = wildcardQuery("foo", "*ba?")
    val result   = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo NOT LIKE "%ba_"""") {
    val operator = "foo" notLike "%ba_"
    val expected = not(wildcardQuery("foo", "*ba?"))
    val result   = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo EXISTS""") {
    val operator = "foo" notNull
    val expected = existsQuery("foo")
    val result   = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo NOT EXISTS""") {
    val operator = "foo" isNull
    val expected = not(existsQuery("foo"))
    val result   = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo IN [1, 2, 3]""") {
    val operator = "foo" in List(1, 2, 3)
    val expected = termsQuery("foo", NonEmptyList.of(1, 2, 3))
    val result   = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo NOT IN [1, 2, 3]""") {
    val operator = "foo" notIn List(1, 2, 3)
    val expected = not(termsQuery("foo", NonEmptyList.of(1, 2, 3)))
    val result   = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo BETWEEN 1 AND 10""") {
    val operator = "foo" between 1 and 10
    val expected = rangeQuery("foo").gte(1).lte(10)
    val result   = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test(""""foo" === "bar" and "baz" === "bar"""") {
    val operator = "foo" === "bar" and "baz" === "bar"
    val expected = boolQuery().must(
      termQuery("foo", "bar"),
      termQuery("baz", "bar")
    )
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test(""""foo" === "bar" or "baz" === "bar"""") {
    val operator = "foo" === "bar" or "baz" === "bar"
    val expected = boolQuery().should(
      termQuery("foo", "bar"),
      termQuery("baz", "bar")
    )
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test(""""user.id" === 123 and "user.status" =!= "inactive"""") {
    val filters = "user.id" === 123 and "user.status" =!= "inactive"
    val expected = boolQuery().must(
      termQuery("user.id", 123),
      boolQuery().not(termQuery("user.status", "inactive"))
    )
    val result = filters.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

}
