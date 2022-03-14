racoon - Search query parser and converter for Scala
=========

![master](https://github.com/moust/racoon/workflows/master/badge.svg)
[![racoon-core Scala version support](https://index.scala-lang.org/moust/racoon/racoon-core/latest-by-scala-version.svg)](https://index.scala-lang.org/moust/racoon/racoon-core)
[<img src="https://img.shields.io/nexus/s/https/oss.sonatype.org/com.moust.racoon/racoon-core_2.13.svg?label=latest%20snapshot&style=plastic"/>](https://oss.sonatype.org/content/repositories/snapshots/com/moust/racoon/)
![Maven Central](https://img.shields.io/maven-central/v/com.moust/racoon-core_2.12.svg)](https://maven-badges.herokuapp.com/maven-central/com.moust/racoon-core_2.12)

**racoon** is a pure functional Scala library for parsing search query strings and converting them for various query language like SQL ([doobie](https://github.com/tpolecat/doobie)) or elasticsearch ([elastic4s](https://github.com/sksamuel/elastic4s)) for example. 

## Quick Start

Tu use **racoon** you need to add the following to your `build.sbt`.

```scala
val racoonVersion = "x.x.x"
libraryDependencies ++= Seq(
  "com.moust" %% "racoon-core" % racoonVersion,
  // Add any of these as needed
  "com.moust" %% "racoon-doobie" % racoonVersion, // doobie algebra
  "com.moust" %% "racoon-elastic4s" % racoonVersion, // elastic4s algebra
)
```

## Parsing search query string

```scala
import com.moust.racoon._

Parser.parse("""user_id = 123 and status = "active"""")
// => Right(And(Eql(Const("user_id"), Value[Int](123)), Eql(Const("status"), Value[String]("active")))
```

## Writing operation

**racoon** define an ADT to write complexe query :

```scala
import com.moust.racoon.operators._

val filters = And(
  Eql(Const("user.status"), Value("active"))
  Or(
    Eql(Const("user.team"), Value("developer")),
    In(Const("user.role"), Values(NonEmptyList.of("admin", "moderator"))),
  )
)
```

## Converting to [doobie](https://github.com/tpolecat/doobie) `Fragment`

```scala
import com.moust.racoon.doobie.implicits._
import com.moust.racoon.operators._
import doobie.Fragment
import doobie.implicits._

val filters = And(
  Eql(Const("user.id"), Value(123)),
  Eql(Const("user.status"), Value("active"))
)
fr"SELECT username WHERE" ++ filters.to[Fragment]
  .query[String]
  .to[List]
  .transact(xa)
// => "SELECT * WHERE user_id = ? AND status = ?"
```

## Converting to [elastic4s](https://github.com/sksamuel/elastic4s) `Query`
```scala
import com.moust.racoon.elastic4s.implicits._
import com.moust.racoon.operators._
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.requests.searches.queries.Query

val filters = And(
  Eql(Const("user.id"), Value(123)),
  Eql(Const("user.status"), Value("active"))
)
search("test").query(filters.to[Query]).show
// => {"query":{"bool":{"must":[{"term":{"user.id":{"value":123}}},{"term":{"user.status":{"value":"active"}}}]}}}
```

## Custom converter

You can write your own converter by extending the trait `ToAlgebra[A]`.

## License
```
This software is licensed under the Apache 2 license, quoted below.

Copyright 2021-2024 Quentin Aupetit

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
```