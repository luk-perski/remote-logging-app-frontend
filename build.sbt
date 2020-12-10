name := """remote-logging-app"""
organization := "pt.iscte_iul.gdsi"
version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.13.1"

libraryDependencies ++=Seq(
	// MySQL connector for Java
	"mysql" % "mysql-connector-java" % "5.1.47",
	// Apache validator utils
	"commons-validator" % "commons-validator" % "1.6",
    // Apache Utils: Java API for Microsoft Documents
    "org.apache.poi" % "poi" % "4.1.0",
    "org.apache.poi" % "poi-ooxml" % "4.1.0",
	// HTML parser and cleaner
	"org.jsoup" % "jsoup" % "1.12.1",
	// BCrypt - secure password storage
	"org.mindrot" % "jbcrypt" % "0.4",
	// Mailer
	"com.typesafe.play" %% "play-mailer" % "7.0.1",
	"com.typesafe.play" %% "play-mailer-guice" % "7.0.1",
	// PDF generation
	"com.itextpdf" % "itextpdf" % "5.5.13.1",
	"com.itextpdf.tool" % "xmlworker" % "5.5.13.1",
	// IP-based location utils
	"com.maxmind.geoip2" % "geoip2" % "2.12.0",
	// Support for Messages in Javascript
	"org.julienrf" %% "play-jsmessages" % "4.0.0",
	// Lombok
	"org.projectlombok" % "lombok" % "1.16.20",
	// Play-related
	guice,
	ehcache,
	javaWs,
	javaJdbc,
	jdbc,
	evolutions
)

PlayKeys.devSettings += "play.server.http.port" -> "9292"

EclipseKeys.preTasks := Seq(compile in Compile)

// Java project. Don't expect Scala IDE
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java

// Use .class files instead of generated .scala files for views and routes
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)
