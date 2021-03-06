package com.citydank.cafeteria

import org.scalajs.dom
import org.scalajs.dom.html
import scala.scalajs.js
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

import scalatags.JsDom.all._
import autowire._
import moment.{Date => MomentDate}

import com.citydank.cafeteria.shared._
import com.citydank.cafeteria.shared.models._
import com.citydank.cafeteria.facades._

object State {
  var date = new js.Date()
  var showSuggestionsForm = false
}

object ScalaJSExample {
  def main(args: Array[String]): Unit = {
    val root = dom.document.getElementById("root")
    root.classList.add("mdc-typography")

    val dateElement = h2().render
    val menuElement = div().render

    renderMenu(State.date, dateElement, menuElement)

    val picker = new MaterialDatetimePicker(js.Dictionary(
      // insert datetime picker within our root element so that the mdc-typography class is applied
      "container" -> root
    ))

    // change value of date and re-render menu when new date is selected
    picker.on(MaterialDatetimePickerEvent.SUBMIT.toString(), (value: js.Any) => {
      val moment = value.asInstanceOf[MomentDate]
      State.date = moment.toDate()
      renderMenu(State.date, dateElement, menuElement)
    })

    // ensure the datetime picker is shown above its scrim div
    picker.on(MaterialDatetimePickerEvent.OPEN.toString(), (_) => {
      val pickerEl = dom.document.getElementsByClassName("c-datepicker").item(0).asInstanceOf[html.Div]
      pickerEl.style.zIndex = "100"
    })

    val suggestionsElement = div().render

    val suggestionsButton = button(
      cls := "mdc-button",
      "Suggest New Dish"
    ).render

    suggestionsButton.onclick = (_) => {
      // update the state variables, and re-render suggestions form and button
      State.showSuggestionsForm = !State.showSuggestionsForm
      suggestionsButton.innerHTML = if (State.showSuggestionsForm) "Hide Form" else "Suggest New Dish"
      renderSuggestionsForm(suggestionsElement, State.showSuggestionsForm)
    }

    val buttons = div(
      button(
        cls := "mdc-button",
        "Select Date",
        onclick := { () => picker.open() }
      ),
      suggestionsButton
    )

    val content = div(
      cls := "mdc-typography",
      h1("CityDank Cafeteria"),
      dateElement,
      menuElement,
      buttons,
      suggestionsElement
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

  def renderSuggestionsForm(suggestionsElement: html.Element, show: Boolean): Unit = {
    suggestionsElement.innerHTML = ""

    if (show) {
      val inputEl = input(id := "inputBox").render

      suggestionsElement.appendChild(
        div(
          div(
            cls := "mdc-form-field mdc-form-field--align-end",
            inputEl,
            label(
              `for` := "inputBox",
              "Dish name"
            )
          ),
          button(
            cls := "mdc-button",
            "Submit",
            onclick := { () => {
              if (inputEl.value.length > 0) {
                submitSuggestion(inputEl.value)
                inputEl.value = ""
              }
            }}
          )
        ).render
      )
    }

    suggestionsElement.render
  }

  def submitSuggestion(dishName: String): Unit = {
    AjaxClient[Api].addDishSuggestion(dishName).call().map(res => {
      println(s"Suggested $dishName, ${if (res) "success!" else "error!"}")
    })
  }
}
