scalaVersion := "2.11.8"

lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  organization := "com.blanchelabs",
  resolvers += Resolver.jcenterRepo,
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    // "-Ywarn-dead-code",
    // "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    // "-Ywarn-unused",
    // "-Ywarn-unused-import",
    "-Xfuture"
  ),
  libraryDependencies ++= Seq(
    )
)

val circeVersion = "0.7.0"

lazy val scalaReflect = Def.setting { "org.scala-lang" % "scala-reflect" % scalaVersion.value }

lazy val serverCore = (project in file("server-core"))
  .settings(commonSettings: _*)
  .dependsOn(commonCore, entitySystem)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel"      %% "cats"                     % "0.9.0",
      "com.lihaoyi"        %% "upickle"                  % "0.4.3",
      "com.twitter"        %% "twitter-server"           % "1.28.0",
      "com.twitter"        %% "util-core"                % "6.42.0",
      "com.twitter"        %% "finagle-stats"            % "6.42.0",
      "com.lihaoyi"        %% "autowire"                 % "0.2.6",
      "com.github.finagle" %% "finch-core"               % "0.14.0",
      "com.github.finagle" %% "finch-circe"              % "0.14.0",
      "io.deepstream"      % "deepstream.io-client-java" % "2.0.8"
    ) ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )

lazy val commonCore = (project in file("common-core"))
  .dependsOn(macros)
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel"        %% "cats"   % "0.9.0",
      "com.esotericsoftware" % "kryonet" % "2.22.0-RC1"
    )
  )

lazy val macros = (project in file("macros"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies += scalaReflect.value
  )

lazy val entitySystem = (project in file("entity-system"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.0" % "test",
      "org.typelevel" %% "cats"      % "0.9.0",
      "com.chuusai"   %% "shapeless" % "2.3.2",
      "com.lihaoyi"   %% "upickle"   % "0.4.3",
      "org.scalameta" %% "scalameta" % "1.7.0"
    )
  )

lazy val clientCore = (project in file("client-core"))
  .dependsOn(commonCore, entitySystem, serverCore)
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel"           %% "cats"         % "0.9.0",
      "com.lihaoyi"             %% "autowire"     % "0.2.6",
      "com.twitter"             %% "util-logging" % "6.43.0",
      "com.googlecode.lanterna" % "lanterna"      % "3.0.0-rc1"
    ) ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )
