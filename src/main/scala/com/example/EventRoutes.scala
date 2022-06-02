package com.example

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import com.example.EventRegistry.{CreateEvent, GetEvents}

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

  def createEvent(name: String): Future[String] =
    eventRegistry.ask(replyTo => CreateEvent(name, replyTo))

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
            },
          )
        },
        path(Segment) { name =>
          concat(
            post {
              complete(createEvent(name))
            }
          )
        })
    }
  //#all-routes
}
