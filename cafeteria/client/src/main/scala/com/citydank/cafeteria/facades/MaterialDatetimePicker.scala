package com.citydank.cafeteria.facades

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.annotation.JSImport.Default

object MaterialDatetimePickerEvent extends Enumeration {
  type EVENT = Value
  val OPEN = Value("open")
  val SUBMIT = Value("submit")
}

@js.native
@JSImport("material-datetime-picker", Default)
class MaterialDatetimePicker(options: js.Any = new js.Object) extends js.Object {
  def open(): Unit = js.native

  def close(): Unit = js.native

  def on(eventName: String, callback: js.Function1[js.Any, js.Any]): Unit = js.native
}