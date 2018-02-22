package com.citydank.cafeteria.shared.models

import upickle.default.{ReadWriter, macroRW}

case class Dish(id: Int, name: String, calories: Int, likes: Int, dislikes: Int)

object Dish {
  implicit def rw: ReadWriter[Dish] = macroRW
}
