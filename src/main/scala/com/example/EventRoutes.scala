package com.example

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import com.example.EventRegistry.GetEvents

import scala.concurrent.Future
// import com.example.EventRegistry._
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout

//#import-json-formats
//#Event-routes-class
class EventRoutes(eventRegistry: ActorRef[EventRegistry.Command])(implicit val system: ActorSystem[_]) {

  //#Event-routes-class
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._
  //#import-json-formats

  // If ask takes more time than this to complete the request is failed
  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getEvents(): Future[Events] =
    eventRegistry.ask(GetEvents)

  //#all-routes
  //#Events-get-post
  //#Events-get-delete
  val eventRoutes: Route =
    pathPrefix("events") {
      concat(
        //#Events-get-delete
        pathEnd {
          concat(
            get {
              complete(getEvents())
            })
        })
      //#Events-get-delete
    }
  //#all-routes
}
