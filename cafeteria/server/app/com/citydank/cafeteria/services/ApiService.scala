package com.citydank.cafeteria.services

import scala.util.Random

import com.citydank.cafeteria.shared._
import com.citydank.cafeteria.shared.models._

// could inject DB here: class ApiService(db: Database) extends Api
class ApiService extends Api {

  // use mock data
  private def dishes = Array[Dish](
    Dish(1, "Tortilla Bowl (deep fried)", 850, 2, 1),
    Dish(2, "Meat and Potatoes", 545, 4, 3),
    Dish(3, "Mac and Cheese", 490, 15, 0),
    Dish(4, "Burger and Fries", 600, 8, 3),
    Dish(5, "Chicken Sandwich", 425, 2, 4)
  )

  // randomly pick a collection of dishes, where each dish appears with a probability of p
  private def randomDishCollection(p: Double): List[Dish] = {
    Random.shuffle[Dish, IndexedSeq](dishes.filter(_ => Random.nextDouble() < p)).toList
  }

  override def menu(year: Int, month: Int, day: Int): Menu = {
    Menu(year, month, day, randomDishCollection(0.7))
  }

  override def addLike(dishId: Int): Int = {
    val idx = dishes.indexWhere(d => d.id == dishId)
    if (idx >= 0) {
      val dish = dishes(idx)
      dishes.update(idx, Dish(dish.id, dish.name, dish.calories, dish.likes + 1, dish.dislikes))
      dishes(idx).likes
    } else {
      -1
    }
  }

  override def addDislike(dishId: Int): Int = {
    val idx = dishes.indexWhere(d => d.id == dishId)
    if (idx >= 0) {
      val dish = dishes(idx)
      dishes.update(idx, Dish(dish.id, dish.name, dish.calories, dish.likes, dish.dislikes + 1))
      dishes(idx).dislikes
    } else {
      -1
    }
  }

  override def addDishSuggestion(dishName: String): Boolean = {
    println(s"Suggested dish $dishName! Too bad we won't actually do anything with this information...")
    true
  }
}
