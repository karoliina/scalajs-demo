package com.citydank.cafeteria

import org.scalajs.dom
import org.scalajs.dom.html
import scala.scalajs.js

import scalatags.JsDom.all._
import autowire._

import com.citydank.cafeteria.shared._
import com.citydank.cafeteria.shared.models._

object ScalaJSExample {
  def main(args: Array[String]): Unit = {
    val root = dom.document.getElementById("root")

    val content = div(
      cls := "mdc-typography",
      h1("CityDank Cafeteria")
    )

    root.appendChild(content.render)
  }
}
