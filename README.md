racoon - Search query parser and converter for Scala
=========

[![Build status](https://img.shields.io/github/workflow/status/moust/racoon/Continuous%20Integration.svg)](https://github.com/moust/racoon/actions)
[![latest snapshot](https://img.shields.io/nexus/s/https/s01.oss.sonatype.org/io.github.moust/racoon-core_2.13.svg?label=latest%20snapshot)](https://s01.oss.sonatype.org/content/repositories/snapshots/io/github/moust/racoon-core_2.12/)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.moust/racoon-core_2.13.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.moust/racoon-core_2.13)
[![javadoc](https://javadoc.io/badge2/io.github.moust/racoon-core_2.13/javadoc.svg)](https://javadoc.io/doc/io.github.moust/racoon-core_2.13)

**racoon** is a pure functional Scala library for parsing search query strings and converting them for various query language like SQL ([doobie](https://github.com/tpolecat/doobie)) or elasticsearch ([elastic4s](https://github.com/sksamuel/elastic4s)) for example. 

## Quick Start

Tu use **racoon** you need to add the following to your `build.sbt`.

```scala
val racoonVersion = "x.x.x"
libraryDependencies ++= Seq(
  "io.github.moust" %% "racoon-core" % racoonVersion,
  // Add any of these as needed
  "io.github.moust" %% "racoon-doobie" % racoonVersion, // doobie algebra
  "io.github.moust" %% "racoon-elastic4s" % racoonVersion // elastic4s algebra
)
```

## Parsing search query string

```scala
import io.github.moust.racoon._

Parser.parse("""user_id = 123 and status = "active"""")
// => Right(And(Eql(Const("user_id"), Value[Int](123)), Eql(Const("status"), Value[String]("active")))
```

## Writing operation

**racoon** define an ADT to write complex query :

```scala
import io.github.moust.racoon.operators._

val filters = And(
  Eql(Const("user.status"), Value("active"))
  Or(
    Eql(Const("user.team"), Value("developer")),
    In(Const("user.role"), Values(List("admin", "moderator"))),
  )
)
```

Or you can use the syntax api to write more readable query :

```scala
import io.github.moust.racoon.implicits._

val filters = ("user.status" === "active") and (("user.team" === "developer") or ("user.role" in List("admin", "moderator")))
// => And(Eql(Const("user.status"), Value("active")), Or(Eql(Const("user.team"), Value("developer")), In(Const("user.role"), Values(List("admin", "moderator")))))
```

## Converting to [doobie](https://github.com/tpolecat/doobie) `Fragment`

```scala
import io.github.moust.racoon.doobie.implicits._
import io.github.moust.racoon.implicits._
import doobie.Fragment
import doobie.implicits._

val filters = "user.id" === 123 and "user.status" =!= "inactive"
fr"SELECT username WHERE" ++ filters.to[Fragment]
  .query[String]
  .to[List]
  .transact(xa)
// => "SELECT * WHERE (user_id = ?) AND (status != ?)"
```

## Converting to [elastic4s](https://github.com/sksamuel/elastic4s) `Query`
```scala
import io.github.moust.racoon.elastic4s.implicits._
import io.github.moust.racoon.implicits._
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.requests.searches.queries.Query

val filters = "user.id" === 123 and "user.status" =!= "inactive"
search("test").query(filters.to[Query]).show
// {
//   "query":{
//     "bool":{
//       "must":[
//         {"term":{"user.id":{"value":123}}},
//         {"bool":{
//           "must_not":[
//             {"term":{"user.status":{"value":"inactive"}}}
//           ]
//         }}
//       ]
//     }
//   }
// }
```

## Custom converter

You can write your own converter by extending the trait `ToAlgebra[A]`.

## License

```
Copyright 2022 Quentin Aupetit

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```