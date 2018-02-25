package com.citydank.cafeteria

import org.scalajs.dom
import org.scalajs.dom.html
import scala.scalajs.js
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

import scalatags.JsDom.all._
import autowire._

import com.citydank.cafeteria.shared._
import com.citydank.cafeteria.shared.models._

object ScalaJSExample {
  def main(args: Array[String]): Unit = {
    val root = dom.document.getElementById("root")

    val dateElement = h2().render
    val menuElement = div().render

    renderMenu(new js.Date(), dateElement, menuElement)

    val content = div(
      cls := "mdc-typography",
      h1("CityDank Cafeteria"),
      dateElement,
      menuElement
    )

    root.appendChild(content.render)
  }

  def renderMenu(date: js.Date, dateEl: html.Element, menuEl: html.Element): Unit = {
    // retrieve menu for the given date through API call
    AjaxClient[Api].menu(date.getFullYear(), date.getMonth() + 1, date.getDate()).call().map((menu: Menu) => {
      // empty menu element so we can append new children
      menuEl.innerHTML = ""
      for (dish <- menu.dishes) menuEl.appendChild(dishCard(dish).render)
      menuEl.render

      dateEl.innerHTML = s"${date.toDateString()}"
      dateEl.render
    })
  }

  def dishCard(dish: Dish) = {
    val styledDiv = div(
      backgroundColor := "lightblue",
      border := "1px solid black",
      borderRadius := "5px",
      margin := 5,
      padding := 10,
      width := 250
    )

    val iconWithMargin = i(marginLeft := 5, marginRight := 5)

    styledDiv(
      div(b(dish.name)),
      div(
        iconWithMargin(cls := "far fa-thumbs-up"),
        span(s"${dish.likes}"),
        iconWithMargin(cls := "far fa-thumbs-down"),
        span(s"${dish.dislikes}")
      ),
      div(s"${dish.calories} kcal")
    )
  }
}
