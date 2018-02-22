package com.citydank.cafeteria.shared.models

import upickle.default.{ReadWriter, macroRW}

case class Menu(year: Int, month: Int, day: Int, dishes: List[Dish])

object Menu {
  implicit def rw: ReadWriter[Menu] = macroRW
}