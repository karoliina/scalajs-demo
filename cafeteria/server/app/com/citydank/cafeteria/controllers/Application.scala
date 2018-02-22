package com.citydank.cafeteria.controllers

import javax.inject._

import play.api.mvc._

@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def bundleUrl(projectName: String, bundleSuffix: String): Option[String] = {
    val name = projectName.toLowerCase
    Seq(s"$name-opt$bundleSuffix.js", s"$name-fastopt$bundleSuffix.js")
      .find(name => getClass.getResource(s"/public/$name") != null)
      .map(controllers.routes.Assets.versioned(_).url)
  }

  def index = Action {
    Ok(views.html.index("CityDank Cafeteria", bundleUrl("client", ""), bundleUrl("client", "-loader"), bundleUrl("client", "-library")))
  }

}
