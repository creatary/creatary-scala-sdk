name := "creatary-scala-sdk" 

version := "1.2" 

organization := "com.creatary"

scalaVersion := "2.9.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= {
	val dispatchVersion = "0.8.7" 
	val liftVersion = "2.4"
	val akkaVersion = "2.0.1"
	Seq(
  		"net.databinder" %% "dispatch-http" % dispatchVersion % "compile",
  		"net.databinder" %% "dispatch-http-json" % dispatchVersion % "compile",
		"junit" % "junit" % "4.10" % "test",
		"org.specs2" %% "specs2" % "1.7.1" % "test",
		"com.novocode" % "junit-interface" % "0.7" % "test->default",
		"net.liftweb" %% "lift-json" % liftVersion,
		"net.liftweb" %% "lift-json-ext" % liftVersion,
		"org.mockito" % "mockito-all" % "1.9.0" % "test",
		"net.liftweb" %% "lift-webkit" % liftVersion,
		"javax.ws.rs" % "jsr311-api" % "1.1.1",
		"com.typesafe.akka" % "akka-actor" % akkaVersion
	)
}

javaOptions += "-Xmx2G"
 
scalacOptions ++= Seq("-deprecation", "-unchecked")

EclipseKeys.withSource := true

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
