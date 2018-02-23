package com.citydank.cafeteria

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom
import upickle.default._
import upickle.Js

object AjaxClient extends autowire.Client[Js.Value, Reader, Writer] {
  override def read[Result: Reader](p: Js.Value) = readJs[Result](p)
  override def write[Result: Writer](r: Result) = writeJs(r)

  override def doCall(req: Request): Future[Js.Value] = {
    // use the Scala.js web API to create a XMLHttpRequest to our API route
    dom.ext.Ajax.post(
      url = "/api/" + req.path.mkString("/"),
      data = upickle.json.write(Js.Obj(req.args.toSeq: _*))
    )
      .map(_.responseText)
      .map(upickle.json.read)
  }
}