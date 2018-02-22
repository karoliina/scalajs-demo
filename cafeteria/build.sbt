lazy val server = (project in file("server"))
  .settings(commonSettings)
  .settings(
    // connect to the client project
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    libraryDependencies ++= Seq(
      guice
    ),
    // serve stylesheets from npm dependencies
    npmAssets ++= NpmAssets.ofProject(client) { nodeModules => nodeModules / "material-components-web/dist/material-components-web.css" }.value,
    npmAssets ++= NpmAssets.ofProject(client) { nodeModules => nodeModules / "material-datetime-picker/dist/material-datetime-picker.css" }.value
  )
  .enablePlugins(PlayScala, WebScalaJSBundlerPlugin)
  .dependsOn(sharedJvm)

lazy val client = (project in file("client"))
  .settings(commonSettings)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    webpackBundlingMode := BundlingMode.LibraryOnly(),
    emitSourceMaps := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.4",
      "com.lihaoyi" %%% "scalatags" % "0.6.7",
      "ru.pavkin" %%% "scala-js-momentjs" % "0.9.1"
    ),
    npmDependencies in Compile ++= Seq(
      "material-components-web" -> "0.29.0",
      "material-datetime-picker" -> "2.4.0"
    )
  )
  .enablePlugins(ScalaJSBundlerPlugin, ScalaJSWeb)
  .dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Seq(
    // dependencies shared between the JS and JVM projects.
    // %%% operator selects the correct version for each project
    "com.lihaoyi" %%% "autowire" % "0.2.6",
    "com.lihaoyi" %%% "upickle" % "0.5.1"
  ))
  .jsConfigure(_.enablePlugins(ScalaJSWeb))

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val commonSettings = Seq(
  scalaVersion := "2.12.4",
  organization := "com.citydank"
)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}
