package com.citydank.cafeteria

import com.citydank.cafeteria.shared.SharedMessages
import org.scalajs.dom

object ScalaJSExample {

  def main(args: Array[String]): Unit = {
    dom.document.getElementById("root").textContent = "CityDank cafeteria"
  }
}
