import org.scalajs.linker.interface.ModuleSplitStyle
import Settings.Libraries._
import scala.sys.process._

val reactJS = "17.0.2"

ThisBuild / Test / bspEnabled                                        := false
ThisBuild / ScalafixConfig / bspEnabled.withRank(KeyRanks.Invisible) := false

addCommandAlias(
  "fixImports",
  "; scalafix OrganizeImports; Test/scalafix OrganizeImports"
)

addCommandAlias(
  "fix",
  "; headerCreateAll; fixImports; scalafmtAll; fixCSS"
)

ThisBuild / description       := "Lucuma-Seed"
Global / onChangedBuildSource := ReloadOnSourceChanges

// Libs common to JVM and JS projects.
lazy val commonLibSettings = Seq(
  libraryDependencies ++=
    Cats.value ++
      CatsEffect.value ++
      Crystal.value ++
      FS2.value ++
      Monocle.value
)

lazy val commonSettings =
  lucumaGlobalSettings ++
    commonLibSettings ++
    Seq(scalacOptions ~= (_.filterNot(Set("-Vtype-diffs"))))

lazy val esModule = Seq(
  scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) },
  Compile / fastLinkJS / scalaJSLinkerConfig ~= { _.withSourceMap(false) },
  Compile / fullLinkJS / scalaJSLinkerConfig ~= { _.withSourceMap(false) },
  Compile / fastLinkJS / scalaJSLinkerConfig ~= (_.withModuleSplitStyle(
    // If the browser is too slow for the SmallModulesFor switch to ModuleSplitStyle.FewestModules
    ModuleSplitStyle.SmallModulesFor(List("explore"))
  )),
  Compile / fullLinkJS / scalaJSLinkerConfig ~= (_.withModuleSplitStyle(
    ModuleSplitStyle.FewestModules
  ))
)

val lintCSS = TaskKey[Unit]("lintCSS", "Lint CSS files")
lintCSS := {
  if (("npm run lint" !) != 0)
    throw new Exception("Error in CSS format")
}

val fixCSS = TaskKey[Unit]("fixCSS", "Fix CSS files")
fixCSS := {
  if (("npm run fix" !) != 0)
    throw new Exception("Error in CSS fix")
}

lazy val root = tlCrossRootProject
  .aggregate(web)

lazy val web: Project = project
  .in(file("web"))
  .settings(commonSettings: _*)
  .settings(esModule: _*)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    Test / test     := {},
    coverageEnabled := false,
    libraryDependencies ++=
      Crystal.value ++
        Log4Cats.value ++
        ReactCommon.value ++
        ScalaJSReact.value
  )
