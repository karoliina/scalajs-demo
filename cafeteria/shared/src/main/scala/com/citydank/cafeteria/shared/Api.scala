package com.citydank.cafeteria.shared

import com.citydank.cafeteria.shared.models._

trait Api {
  def menu(year: Int, month: Int, day: Int): Menu

  def addLike(dishId: Int): Int // returns new like count

  def addDislike(dishId: Int): Int // returns new dislike count

  def addDishSuggestion(dishName: String): Boolean // true if succeeded, false if failed
}