name := "Project1"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.13"
libraryDependencies += "com.typesafe.akka" % "akka-remote_2.11" % "2.3.13"
libraryDependencies += "com.typesafe" % "config" % "1.2.1"
libraryDependencies += "io.netty" % "netty" % "3.8.0.Final"
libraryDependencies += "com.google.protobuf" % "protobuf-java" % "2.5.0"
