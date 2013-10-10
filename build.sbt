name := "stopwatch"

version := "1.0.0"

scalaVersion := "2.10.3"

mainClass in Compile := Some("com.yumusoft.stopwatch.Main")

unmanagedJars in Compile += Attributed.blank(
    file(scala.util.Properties.javaHome) / "lib" / "jfxrt.jar")

fork in run := true

//libraryDependencies += "org.scalafx" %% "scalafx-core" % "1.0-SNAPSHOT"

