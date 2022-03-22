package racoon.util

import atto.Atto._
import atto.Parser
import cats.implicits._

import java.time.{LocalDate, LocalDateTime, LocalTime, ZonedDateTime, ZoneId}

trait DateTimeParser {

  implicit class ParserCharsOps(p: Parser[List[Char]]) {
    def toInt: Parser[Int] = p.map(_.mkString).flatMap { s =>
      Either.catchOnly[NumberFormatException](s.toInt) match {
        case Right(num) => ok(num)
        case Left(e)    => err(e.toString)
      }
    }
  }

  private def fixed(n: Int): Parser[Int] =
    count(n, digit).toInt

  val zoneId: Parser[ZoneId] = (
    string("Z") | (for {
      sign   <- char('+') | char('+')
      hour   <- count(2, digit).map(_.mkString) <~ char(':')
      minute <- count(2, digit).map(_.mkString)
    } yield s"$sign$hour:$minute")
  ).map(ZoneId.of)

  val localTime: Parser[LocalTime] = (
    fixed(2) <~ char(':'),
    fixed(2) <~ char(':'),
    fixed(2),
    opt(char('.') ~> many(digit).map(_.padTo(9, '0')).toInt)
  ).mapN {
    case (hour, minute, second, None)               => LocalTime.of(hour, minute, second)
    case (hour, minute, second, Some(nanoOfSecond)) => LocalTime.of(hour, minute, second, nanoOfSecond)
  }

  val localDate: Parser[LocalDate] = (fixed(4) <~ char('-'), fixed(2) <~ char('-'), fixed(2)).mapN(LocalDate.of)

  val localDateTime: Parser[LocalDateTime] = (
    localDate <~ (char(' ') | char('T')),
    localTime
  ).mapN(LocalDateTime.of)

  val zonedDateTime: Parser[ZonedDateTime] = (
    localDate <~ (char(' ') | char('T')),
    localTime,
    zoneId
  ).mapN(ZonedDateTime.of)

}
