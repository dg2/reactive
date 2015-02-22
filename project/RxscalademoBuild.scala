import sbt._
import sbt.Keys._

object RxscalademoBuild extends Build {

  lazy val rxscalademo = Project(
    id = "rxscalademo",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Reactive",
      organization := "info.dg2",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.4"
      // add other settings here
    )
  )
}
