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

/**
 * Top-level import, providing aliases for the most commonly used types and modules from
 * racoon-core. A typical starting set of imports would be something like this.
 * {{{
 * import racoon._, racoon.implicits._
 * }}}
 */
package object racoon
  extends Aliases {

  /** Top-level import for all instances and syntax provided by doobie-free and doobie-core. */
  object implicits
    extends syntax.AllSyntax

}