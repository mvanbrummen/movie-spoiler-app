lazy val akkaHttpVersion = "10.0.11"
lazy val akkaVersion    = "2.5.11"

lazy val root = (project in file("."))
  .aggregate(backend)

lazy val backend = project
  .enablePlugins(JavaAppPackaging) // required for docker packaging
  .settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.12.4"
    )),
    name := "movie-spoiler-app",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,

      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.1"         % Test
    ),
    dockerRepository := Some("docker.io"), // docker hub
    dockerUsername := Some("mvanbrummen") // your docker hub username
  )

