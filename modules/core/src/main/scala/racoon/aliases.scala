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

package racoon

/** Mixin containing aliases for the most commonly used types and modules from core */
trait Aliases extends Types with Modules

/** Mixn containing aliases for the most commonly used types from core */
trait Types {
  type Algebra[A]   = racoon.util.Algebra[A]
  type ToAlgebra[A] = racoon.util.ToAlgebra[A]
  type Op           = racoon.util.operators.Op
}

/** Mixn containing aliases for the most commonly used mocules from core */
trait Modules {
  val Parser: racoon.util.parser.type       = racoon.util.parser
  val operators: racoon.util.operators.type = racoon.util.operators
}
