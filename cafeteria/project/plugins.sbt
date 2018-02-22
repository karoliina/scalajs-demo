// Comment to get more information during initialization
logLevel := Level.Warn

// Resolvers
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

// Sbt plugins
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.11")

// scala.js compiler plugin
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.22")

// full stack scala integration
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.6")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.4")

// scalajs-bundler for managing js dependencies
addSbtPlugin("ch.epfl.scala" % "sbt-web-scalajs-bundler" % "0.10.0")