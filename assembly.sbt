import AssemblyKeys._ // put this at the top of the file

assemblySettings

jarName in assembly := "fx-stopwatch.jar"

test in assembly := {}

mainClass in assembly := Some("com.yumusoft.stopwatch.Main")

