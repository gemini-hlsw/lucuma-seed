// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.seed.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import react.common._
import scala.scalajs.js
import js.annotation._
import scala.annotation.nowarn

final case class Button(
  val label:              js.UndefOr[String] = js.undefined,
  override val modifiers: Seq[TagMod] = Seq.empty
) extends GenericComponentPAC[Button.ButtonProps, Button] {
  override protected def cprops                     = Button.props(this)
  override protected val component                  = Button.component
  override def addModifiers(modifiers: Seq[TagMod]) = copy(modifiers = this.modifiers ++ modifiers)
}

@nowarn
object Button {
  @js.native
  @JSImport("primereact/button", "Button")
  object RawComponent extends js.Function1[js.Any, js.Any] {
    def apply(i: js.Any): js.Any = js.native
  }

  @js.native
  trait ButtonProps extends js.Object {
    var label: js.UndefOr[String] = js.native

  }

  def props(q: Button): ButtonProps =
    rawprops(q.label)

  def rawprops(label: js.UndefOr[String]): ButtonProps = {
    val p = (new js.Object()).asInstanceOf[ButtonProps]
    label.foreach(v => p.label = v)
    p
  }

  private val component =
    JsComponent[ButtonProps, Children.Varargs, Null](RawComponent)
}
