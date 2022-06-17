// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.seed

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Sync
import crystal.react.Ctx
import japgolly.scalajs.react.callback.CallbackTo
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import log4cats.loglevel.LogLevelLogger
import org.scalajs.dom
import org.scalajs.dom.Element
import org.typelevel.log4cats.Logger
import typings.loglevel.mod.LogLevelDesc

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.annotation.JSExportTopLevel

object AppCtx extends Ctx[CallbackTo, AppContext[IO]] {}

@JSExportTopLevel("Main")
object Main extends IOApp.Simple {

  @JSExport
  def resetIOApp(): Unit =
    // https://github.com/typelevel/cats-effect/pull/2114#issue-687064738
    cats.effect.unsafe.IORuntime.asInstanceOf[{ def resetGlobal(): Unit }].resetGlobal()

  @JSExport
  def runIOApp(): Unit = main(Array.empty)

  def setupLogger[F[_]: Sync](level: LogLevelDesc): F[Logger[F]] = Sync[F].delay {
    LogLevelLogger.setLevel(level)
    LogLevelLogger.createForRoot[F]
  }

  def setupDOM[F[_]: Sync]: F[Element] = Sync[F].delay(
    Option(dom.document.getElementById("root")).getOrElse {
      val elem = dom.document.createElement("div")
      elem.id = "root"
      dom.document.body.appendChild(elem)
      elem
    }
  )

  val (router, routerCtl) =
    Router.componentAndCtl(BaseUrl.fromWindowOrigin, Routing.config)

  def buildPage(implicit logger: Logger[IO]): IO[Unit] =
    (for {
      ctx  <- IO(AppContext[IO]())
      node <- setupDOM[IO]
    } yield AppContext.ctx.provide(ctx)(router()).renderIntoDOM(node)).void

  override final def run: IO[Unit] =
    for {
      logger <- setupLogger[IO](LogLevelDesc.DEBUG)
      _      <- buildPage(logger)
    } yield ()
}