import sbt.Def
import org.portablescala.sbtplatformdeps.PlatformDepsGroupArtifactID
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt.librarymanagement._

object Settings {

  object LibraryVersions {
    val cats               = "2.8.0"
    val catsEffect         = "3.3.13"
    val crystal            = "0.0.0+1-8dd5205c-SNAPSHOT"
    val fs2                = "3.2.9"
    val log4Cats           = "2.3.2"
    val log4CatsLogLevel   = "0.3.1"
    val lucumaUI           = "0.36.3"
    val monocle            = "3.1.0"
    val reactCommon        = "0.17.0"
    val scalaJsReact       = "2.1.1"
    val lucumaReactVersion = "1.0-0f7eb8e-SNAPSHOT"
  }

  object Libraries {
    import LibraryVersions._

    private def deps(modules: PlatformDepsGroupArtifactID*)(version: String): Seq[ModuleID] =
      modules.map(_ % version)

    def In(configuration: Configuration)(dependencies: Seq[ModuleID]): Seq[ModuleID] =
      dependencies.map(_ % configuration)

    val Cats = Def.setting(
      deps(
        "org.typelevel" %%% "cats-core"
      )(cats)
    )

    val CatsEffect = Def.setting(
      deps(
        "org.typelevel" %%% "cats-effect"
      )(catsEffect)
    )

    val Crystal = Def.setting(
      deps(
        "com.rpiaggio" %%% "crystal"
      )(crystal)
    )

    val FS2 = Def.setting(
      deps(
        "co.fs2" %%% "fs2-core"
      )(fs2)
    )

    val Log4Cats = Def.setting(
      Seq(
        "org.typelevel" %%% "log4cats-core"     % log4Cats,
        "com.rpiaggio"  %%% "log4cats-loglevel" % log4CatsLogLevel
      )
    )

    val Monocle = Def.setting(
      deps(
        "dev.optics" %%% "monocle-core",
        "dev.optics" %%% "monocle-macro",
        "dev.optics" %%% "monocle-unsafe"
      )(monocle)
    )

    val ReactCommon = Def.setting(
      deps(
        "edu.gemini" %%% "lucuma-react-common",
        "edu.gemini" %%% "lucuma-react-cats"
      )(lucumaReactVersion)
    )

    val ScalaJSReact = Def.setting(
      deps(
        "com.github.japgolly.scalajs-react" %%% "core-bundle-cb_io",
        "com.github.japgolly.scalajs-react" %%% "extra",
        "com.github.japgolly.scalajs-react" %%% "extra-ext-monocle3",
        "com.github.japgolly.scalajs-react" %%% "callback-ext-cats_effect"
      )(scalaJsReact)
    )
  }
}
