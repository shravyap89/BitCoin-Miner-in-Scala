name := "DEMO1"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-remote" % "2.3.13"
  ,"com.typesafe.akka" %% "akka-actor" % "2.3.13"
  ,"com.typesafe.akka" %% "config" % "1.2.1"
  ,"com.typesafe.akka" %% "netty" % "3.8.0"
  ,"com.typesafe.akka" %% "protobuf-java" % "2.5.0"
)
