package com.example

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import scala.util.Failure
import scala.util.Success

// 機能一覧
// 1. イベントの作成ができる
//    POST /events/event_name { "ticket": 250 }
// 2. 全イベントの取得ができる
// 3. チケットの購入ができる
// 4. イベントのキャンセルができる

//#main-class
object QuickstartApp {
  //#start-http-server
  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    import system.executionContext

    val futureBinding = Http().newServerAt("localhost", 8080).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
  //#start-http-server
  def main(args: Array[String]): Unit = {
    //#server-bootstrapping
    val rootBehavior = Behaviors.setup[Nothing] { context =>
//      val userRegistryActor = context.spawn(UserRegistry(), "UserRegistryActor")
//      context.watch(userRegistryActor)

      val eventRegistryActor = context.spawn(EventRegistry(), "EventRegistryActor")
      context.watch(eventRegistryActor)

      val routes = new EventRoutes(eventRegistryActor)(context.system)
      startHttpServer(routes.eventRoutes)(context.system)

      Behaviors.empty
    }
    val system = ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
    //#server-bootstrapping
  }
}
//#main-class
