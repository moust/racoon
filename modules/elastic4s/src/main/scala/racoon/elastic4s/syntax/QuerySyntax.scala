package racoon.elastic4s.syntax

import com.sksamuel.elastic4s.requests.searches.queries.RangeQuery

trait QueryOps {

  implicit class RangeQueryOps(query: RangeQuery) {

    def gt0[A]: PartialFunction[A, RangeQuery] = {
      case gt: Byte => query.gt(gt.toLong)
      case gt: Short => query.gt(gt.toLong)
      case gt: Int => query.gt(gt.toLong)
      case gt: Long => query.gt(gt)
      case gt: Float => query.gt(gt.toDouble)
      case gt: Double => query.gt(gt)
      case gt: String => query.gt(gt)
    }

    def gte0[A]: PartialFunction[A, RangeQuery] = {
      case gte: Byte => query.gte(gte.toLong)
      case gte: Short => query.gte(gte.toLong)
      case gte: Int => query.gte(gte.toLong)
      case gte: Long => query.gte(gte)
      case gte: Float => query.gte(gte.toDouble)
      case gte: Double => query.gte(gte)
      case gte: String => query.gte(gte)
    }

    def lt0[A]: PartialFunction[A, RangeQuery] = {
      case lt: Byte => query.lt(lt.toLong)
      case lt: Short => query.lt(lt.toLong)
      case lt: Int => query.lt(lt.toLong)
      case lt: Long => query.lt(lt)
      case lt: Float => query.lt(lt.toDouble)
      case lt: Double => query.lt(lt)
      case lt: String => query.lt(lt)
    }

    def lte0[A]: PartialFunction[A, RangeQuery] = {
      case lte: Byte => query.lte(lte.toLong)
      case lte: Short => query.lte(lte.toLong)
      case lte: Int => query.lte(lte.toLong)
      case lte: Long => query.lte(lte)
      case lte: Float => query.lte(lte.toDouble)
      case lte: Double => query.lte(lte)
      case lte: String => query.lte(lte)
    }

  }

}

object query extends QueryOps