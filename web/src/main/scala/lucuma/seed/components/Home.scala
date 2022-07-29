// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.seed.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import lucuma.seed.AppContext
import react.common.ReactFnProps
import react.common.syntax.all._
// import react.datepicker.Datepicker
// import react.semanticui.elements.button.Button
// import lucuma.reactDatepicker.components.ReactDatepicker
// import reactST.primereact.components.InputText
import reactST.primereact.components.Tag
import reactST.primereact.tagMod._

final case class Home() extends ReactFnProps[Home](Home.component)

object Home {
  protected type Props = Home

  val tagProps = new TagProps { value = "what?" }

  protected val component =
    ScalaFnComponent
      .withHooks[Props]
      .useContext(AppContext.ctx)
      .useEffectBy { (_, ctx) =>
        ctx.logger.debug("Rendering Home component") // Running an IO in useEffect
      }
      .useState(0)
      .render { (_, _, clicks) =>
        <.div(
          <.p("HELLO WORLD!"),
          <.p(
            s"You clicked ${clicks.value} time(s).",
            <.button("Click me!", ^.onClick --> clicks.modState(_ + 1))
          ),
          // Button(content = "Semantic UI Button"),
          // ReactDatepicker((_, _) => Callback.empty).value("01/01/2021"),
          // Datepicker((_, _) => Callback.empty)
          Tag.withProps(tagProps)
          // Tag.value("Yo!")
          // InputText.value("Hi!")
        )
      }
}
