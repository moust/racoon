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

package racoon.elastic4s.syntax

import java.time.{LocalDate, LocalDateTime, LocalTime, ZonedDateTime}
import java.time.format.DateTimeFormatter

import com.sksamuel.elastic4s.requests.searches.queries.RangeQuery

trait QueryOps {

  implicit class RangeQueryOps(query: RangeQuery) {

    def gt0[A]: PartialFunction[A, RangeQuery] = {
      case gt: Byte          => query.gt(gt.toLong)
      case gt: Short         => query.gt(gt.toLong)
      case gt: Int           => query.gt(gt.toLong)
      case gt: Long          => query.gt(gt)
      case gt: Float         => query.gt(gt.toDouble)
      case gt: Double        => query.gt(gt)
      case gt: String        => query.gt(gt)
      case gt: LocalTime     => query.gt(gt.format(DateTimeFormatter.ISO_LOCAL_TIME))
      case gt: LocalDate     => query.gt(gt.format(DateTimeFormatter.ISO_LOCAL_DATE))
      case gt: LocalDateTime => query.gt(gt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
      case gt: ZonedDateTime => query.gt(gt.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
    }

    def gte0[A]: PartialFunction[A, RangeQuery] = {
      case gte: Byte          => query.gte(gte.toLong)
      case gte: Short         => query.gte(gte.toLong)
      case gte: Int           => query.gte(gte.toLong)
      case gte: Long          => query.gte(gte)
      case gte: Float         => query.gte(gte.toDouble)
      case gte: Double        => query.gte(gte)
      case gte: String        => query.gte(gte)
      case gte: LocalTime     => query.gte(gte.format(DateTimeFormatter.ISO_LOCAL_TIME))
      case gte: LocalDate     => query.gte(gte.format(DateTimeFormatter.ISO_LOCAL_DATE))
      case gte: LocalDateTime => query.gte(gte.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
      case gte: ZonedDateTime => query.gte(gte.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
    }

    def lt0[A]: PartialFunction[A, RangeQuery] = {
      case lt: Byte          => query.lt(lt.toLong)
      case lt: Short         => query.lt(lt.toLong)
      case lt: Int           => query.lt(lt.toLong)
      case lt: Long          => query.lt(lt)
      case lt: Float         => query.lt(lt.toDouble)
      case lt: Double        => query.lt(lt)
      case lt: String        => query.lt(lt)
      case lt: LocalTime     => query.lt(lt.format(DateTimeFormatter.ISO_LOCAL_TIME))
      case lt: LocalDate     => query.lt(lt.format(DateTimeFormatter.ISO_LOCAL_DATE))
      case lt: LocalDateTime => query.lt(lt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
      case lt: ZonedDateTime => query.lt(lt.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
    }

    def lte0[A]: PartialFunction[A, RangeQuery] = {
      case lte: Byte          => query.lte(lte.toLong)
      case lte: Short         => query.lte(lte.toLong)
      case lte: Int           => query.lte(lte.toLong)
      case lte: Long          => query.lte(lte)
      case lte: Float         => query.lte(lte.toDouble)
      case lte: Double        => query.lte(lte)
      case lte: String        => query.lte(lte)
      case lte: LocalTime     => query.lte(lte.format(DateTimeFormatter.ISO_LOCAL_TIME))
      case lte: LocalDate     => query.lte(lte.format(DateTimeFormatter.ISO_LOCAL_DATE))
      case lte: LocalDateTime => query.lte(lte.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
      case lte: ZonedDateTime => query.lte(lte.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
    }

  }

}

object query extends QueryOps
