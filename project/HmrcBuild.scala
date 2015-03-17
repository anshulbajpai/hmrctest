/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import sbt._
import sbt.Keys._

object HmrcBuild extends Build {

  import uk.gov.hmrc._
  import DefaultBuildSettings._
  import uk.gov.hmrc.{SbtBuildInfo, ShellPrompt}
  import scala.util.Properties.envOrElse

  val appName = "hmrctest"
  val appVersion = envOrElse("HMRCTEST_VERSION", "1.0.0-SNAPSHOT")

  lazy val microservice = Project(appName, file("."))
    .settings(version := appVersion)
    .settings(scalaSettings: _*)
    .settings(defaultSettings(): _*)
    .settings(publishArtifact := true)
    .settings(
      targetJvm := "jvm-1.7",
      shellPrompt := ShellPrompt(appVersion),
      libraryDependencies ++= AppDependencies(),
      resolvers := Seq(
        Opts.resolver.sonatypeReleases,
        Opts.resolver.sonatypeSnapshots,
        "typesafe-releases" at "http://repo.typesafe.com/typesafe/releases/",
        "typesafe-snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
      ),
      crossScalaVersions := Seq("2.11.6", "2.11.5")
    )
    .settings(SbtBuildInfo(): _*)
    .settings(BintraySettings(Some("HMRC")): _*)
    .settings(BuildDescriptionSettings(): _*)
}

private object AppDependencies {

  import play.PlayImport._
  import play.core.PlayVersion

  val compile = Seq(
    "com.typesafe.play" %% "play" % PlayVersion.current,
    ws % "provided",

    "org.scalatest" %% "scalatest" % "2.2.2",
    "com.typesafe.play" %% "play-test" % PlayVersion.current,
    "org.pegdown" % "pegdown" % "1.4.2"

  )

  def apply() = compile
}

object BintraySettings {

  import bintray.Plugin._
  import bintray.Keys._

  def apply(org : Option[String]) = bintrayPublishSettings ++ Seq (
    publishMavenStyle := false,
    repository in bintray := "releases",
    bintrayOrganization in bintray := org,
    licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
  )
}

object BuildDescriptionSettings {

  def apply() =
    Seq(
      pomExtra := (<url>https://www.gov.uk/government/organisations/hm-revenue-customs</url>
        <licenses>
          <license>
            <name>Apache 2</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
          </license>
        </licenses>
        <scm>
          <connection>scm:git@github.com:hmrc/hmrctest.git</connection>
          <developerConnection>scm:git@github.com:hmrc/hmrctest.git</developerConnection>
          <url>scm:git@github.com:hmrc/hmrctest.git</url>
        </scm>
        <developers>
          <developer>
            <id>charleskubicek</id>
            <name>Charles Kubicek</name>
            <url>http://www.equalexperts.com</url>
          </developer>
          <developer>
            <id>duncancrawford</id>
            <name>Duncan Crawford</name>
            <url>http://www.equalexperts.com</url>
          </developer>
          <developer>
            <id>xnejp03</id>
            <name>Petr Nejedly</name>
            <url>http://www.equalexperts.com</url>
          </developer>
        </developers>)
    )

}

