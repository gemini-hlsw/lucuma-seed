// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.seed

sealed trait Page extends Product with Serializable

object Page {
  case object HomePage extends Page
}
