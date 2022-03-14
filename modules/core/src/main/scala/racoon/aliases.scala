package racoon

/** Mixin containing aliases for the most commonly used types and modules from core */
trait Aliases extends Types with Modules

/** Mixn containing aliases for the most commonly used types from core */
trait Types {
  type Algebra[A]   = racoon.util.Algebra[A]
  type ToAlgebra[A] = racoon.util.ToAlgebra[A]
  type Op = racoon.util.operators.Op
}

/** Mixn containing aliases for the most commonly used mocules from core */
trait Modules {
  val Parser = racoon.util.parser
  val operators = racoon.util.operators
}
