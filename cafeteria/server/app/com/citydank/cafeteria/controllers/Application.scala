package com.citydank.cafeteria.controllers

import javax.inject._
import play.api.mvc._
import upickle.default._
import upickle.Js

import scala.concurrent.ExecutionContext.Implicits.global

import com.citydank.cafeteria.services.ApiService
import com.citydank.cafeteria.shared.Api

// serialization instructions for autowire
object Router extends autowire.Server[Js.Value, Reader, Writer] {
  override def read[Result: Reader](p: Js.Value) = readJs[Result](p)
  override def write[Result: Writer](r: Result) = writeJs(r)
}

@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val apiService = new ApiService()

  def bundleUrl(projectName: String, bundleSuffix: String): Option[String] = {
    val name = projectName.toLowerCase
    Seq(s"$name-opt$bundleSuffix.js", s"$name-fastopt$bundleSuffix.js")
      .find(name => getClass.getResource(s"/public/$name") != null)
      .map(controllers.routes.Assets.versioned(_).url)
  }

  def index = Action {
    Ok(views.html.index("CityDank Cafeteria", bundleUrl("client", ""), bundleUrl("client", "-loader"), bundleUrl("client", "-library")))
  }

  def autowireApi(path: String) = Action.async(parse.text) {
    implicit request =>
      println(s"Request path: $path")

      // get the request body as ByteString
      val s = request.body.toString()

      Router.route[Api](apiService)(
        // deserialize request body and call server route
        autowire.Core.Request(path.split("/"), upickle.json.read(s).asInstanceOf[Js.Obj].value.toMap)
      ).map(res => {
        // serialize response and send to client
        Ok(upickle.json.write(res))
      })
  }

}
