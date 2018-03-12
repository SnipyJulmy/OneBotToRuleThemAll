name := "OneBotToRuleThemAll"

version := "0.1"

scalaVersion := "2.12.4"

resolvers += Resolver.JCenterRepository

libraryDependencies ++= Seq(
  "info.mukel" %% "telegrambot4s" % "3.0.14",
  "io.spray" %% "spray-json" % "1.3.4",
  "net.dean.jraw" % "JRAW" % "1.0.0"
)
