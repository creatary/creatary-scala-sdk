name := "creatary-scala-sdk" 

version := "1.0" 

scalaVersion := "2.9.1"

libraryDependencies ++= {
	val dispatchVersion = "0.8.7" 
	val liftVersion = "2.4"
	Seq(
  		"net.databinder" %% "dispatch-http" % dispatchVersion % "compile",
  		"net.databinder" %% "dispatch-http-json" % dispatchVersion % "compile",
		"junit" % "junit" % "4.10" % "test",
		"org.specs2" %% "specs2" % "1.7.1" % "test",
		"com.novocode" % "junit-interface" % "0.7" % "test->default",
		"net.liftweb" %% "lift-json" % liftVersion,
		"org.mockito" % "mockito-all" % "1.9.0" % "test"
	)
}

javaOptions += "-Xmx2G"
 
scalacOptions ++= Seq("-deprecation", "-unchecked")

EclipseKeys.withSource := true