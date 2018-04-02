val buildName = "jzlib"
val buildOrganization = "com.jcraft"

lazy val root = Project(buildName, file("."))
  .settings(
    name          := buildName,
    organization  := buildOrganization,
    version       := "1.1.4-SNAPSHOT",
    scalaVersion  := "2.11.8",
    crossPaths    := false,
    javaOptions   ++= Seq (
      "-target", "1.8"
    ),

    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.5"
    ),

    parallelExecution in Test := false,
    testOptions in Test ++= Seq(
      Tests.Argument("-oDF"),
      Tests.Argument(TestFrameworks.ScalaTest, "-u", "%s" format ((target in Test).value / "test-reports"))
    )
  )
