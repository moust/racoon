package racoon.elastic4s

import cats.data.NonEmptyList
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.ElasticProperties
import com.sksamuel.elastic4s.requests.searches.queries.Query
import racoon.elastic4s.implicits._
import racoon.operators._

class Elastic4sAlgebraSuite extends munit.FunSuite {

  test("""foo = "bar"""") {
    val operator = Eql(Const("foo"), Value("bar"))
    val expected = termQuery("foo", "bar")
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo != "bar"""") {
    val operator = NotEql(Const("foo"), Value("bar"))
    val expected = not(termQuery("foo", "bar"))
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo > 0""") {
    val operator = Gt(Const("foo"), Value(0))
    val expected = rangeQuery("foo").gt(0)
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo < 100""") {
    val operator = Lt(Const("foo"), Value(100))
    val expected = rangeQuery("foo").lt(100)
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo >= 0""") {
    val operator = Gte(Const("foo"), Value(0))
    val expected = rangeQuery("foo").gte(0)
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo <= 100""") {
    val operator = Lte(Const("foo"), Value(100))
    val expected = rangeQuery("foo").lte(100)
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo LIKE "%ba_"""") {
    val operator = Like(Const("foo"), Value("%ba_"))
    val expected = wildcardQuery("foo", "*ba?")
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo NOT LIKE "%bar%"""") {
    val operator = NotLike(Const("foo"), Value("%ba_"))
    val expected = not(wildcardQuery("foo", "*ba?"))
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo EXISTS""") {
    val operator = Exists(Const("foo"))
    val expected = existsQuery("foo")
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo NOT EXISTS""") {
    val operator = NotExists(Const("foo"))
    val expected = not(existsQuery("foo"))
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo IN [1, 2, 3]""") {
    val operator = In(Const("foo"), Values(NonEmptyList.of(Value(1), Value(2), Value(3))))
    val expected = termsQuery("foo", NonEmptyList.of(1, 2, 3))
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo NOT IN [1, 2, 3]""") {
    val operator = NotIn(Const("foo"), Values(NonEmptyList.of(Value(1), Value(2), Value(3))))
    val expected = not(termsQuery("foo", NonEmptyList.of(1, 2, 3)))
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""foo BETWEEN 1 AND 10""") {
    val operator = Between(Const("foo"), Value(1), Value(10))
    val expected = rangeQuery("foo").gte(1).lte(10)
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""(foo = "bar") AND (baz = "bar")""") {
    val operator = And(Eql(Const("foo"), Value("bar")), Eql(Const("baz"), Value("bar")))
    val expected = boolQuery().must(
      termQuery("foo", "bar"),
      termQuery("baz", "bar")
    )
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

  test("""(foo = "bar") OR (baz = "bar")""") {
    val operator = Or(Eql(Const("foo"), Value("bar")), Eql(Const("baz"), Value("bar")))
    val expected = boolQuery().should(
      termQuery("foo", "bar"),
      termQuery("baz", "bar")
    )
    val result = operator.to[Query]
    assertNoDiff(result.toString, expected.toString)
  }

}