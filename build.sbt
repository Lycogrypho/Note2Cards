organization := "it.grypho"

ThisBuild / version := "0.2.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.3"

//ThisBuild / assemblyMergeStrategy := {
//    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//    case x => MergeStrategy.first
//}


lazy val root = (project in file("."))
  .settings(
    name := "Note2Cards",
    )


//libraryDependencies +="org.scalatest" %% "scalatest" % "3.0.5" % Test

// https://mvnrepository.com/artifact/org.apache.tika/tika-core
libraryDependencies += "org.apache.tika" % "tika-core" % "2.5.0"
// https://mvnrepository.com/artifact/org.apache.tika/tika-parsers-standard-package
libraryDependencies += "org.apache.tika" % "tika-parsers-standard-package" % "2.5.0"
// https://mvnrepository.com/artifact/org.apache.tika/tika-parsers
libraryDependencies += "org.apache.tika" % "tika-parsers" % "2.5.0" pomOnly()
// https://mvnrepository.com/artifact/org.apache.tika/tika-parser-pdf-module
libraryDependencies += "org.apache.tika" % "tika-parser-pdf-module" % "2.5.0"
// https://mvnrepository.com/artifact/org.apache.tika/tika-parser-image-module
libraryDependencies += "org.apache.tika" % "tika-parser-image-module" % "2.5.0"
// https://mvnrepository.com/artifact/org.apache.tika/tika-parser-pdf-module
libraryDependencies += "org.apache.tika" % "tika-parser-pdf-module" % "2.5.0"
// https://mvnrepository.com/artifact/org.apache.tika/tika-xmp
libraryDependencies += "org.apache.tika" % "tika-xmp" % "2.5.0"
// https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.19.0"

// https://mvnrepository.com/artifact/org.scala-lang.modules/scala-swing
//libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.1.1"
// https://mvnrepository.com/artifact/org.scalanlp/breeze
//libraryDependencies += "org.scalanlp" %% "breeze" % "1.0"
// https://mvnrepository.com/artifact/org.scala-lang.modules/scala-swing
//libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.1.1"
// https://mvnrepository.com/artifact/org.jsoup/jsoup
//libraryDependencies += "org.jsoup" % "jsoup" % "1.12.2"

//libraryDependencies += "com.typesafe.slick" %% "slick" % "3.3.2" // https://mvnrepository.com/artifact/com.typesafe.slick/slick
//libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25" // Richiesto da Slick, sostituire con un logger non-nop se deve essere letto il log errori
//libraryDependencies += "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2" // Richiesto da Slick
//libraryDependencies += "com.h2database" % "h2" % "1.4.187" // Richiesto da Slick
//libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.8.7" // Richiesto da Slick

// https://mvnrepository.com/artifact/org.scalafx/scalafx
libraryDependencies += "org.scalafx" %% "scalafx" % "19.0.0-R30" // Add dependency on ScalaFX library
// https://mvnrepository.com/artifact/org.scalafx/scalafx-extras
libraryDependencies += "org.scalafx" %% "scalafx-extras" % "0.7.0"


// apache commons per conversione base64
// libraryDependencies += "commons-codec" % "commons-codec" % "1.14" 	        // https://mvnrepository.com/artifact/commons-codec/commons-codec
// libraryDependencies += "org.apache.commons" % "org.apache.commons.codec" % "1.3.0-20081006" // https://mvnrepository.com/artifact/org.apache.commons/org.apache.commons.codec
// libraryDependencies += "at.bestsolution.efxclipse.eclipse" % "org.apache.commons.codec" % "1.6.0"    // https://mvnrepository.com/artifact/at.bestsolution.efxclipse.eclipse/org.apache.commons.codec
// https://mvnrepository.com/artifact/org.eclipse.ecf/org.apache.commons.codec
//	@FindBy(css="")
//	private WebElement webElement;
//libraryDependencies += "org.eclipse.ecf" % "org.apache.commons.codec" % "1.9.0.v20170208-1614"

// Browser component
// libraryDependencies += "chrriis" % "DJNativeSwing" % "1.0.2" // https://mvnrepository.com/artifact/chrriis/DJNativeSwing
//libraryDependencies += "com.teamdev.jxbrowser" % "jxbrowser-swing" % "7.0" //http://maven.icm.edjxu.pl/artifactory/repo/   https://mvnrepository.com/artifact/com.teamdev.jxbrowser/jxbrowser-swing
// https://mvnrepository.com/artifact/com.teamdev.jxbrowser/jxbrowser-cross-platform
//libraryDependencies += "com.teamdev.jxbrowser" % "jxbrowser-cross-platform" % "7.0"

lazy val app = (project in file("Note2Cards"))
  .settings(
    assembly / assemblyJarName := s"${name.value}-${version.value}"
    )


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}