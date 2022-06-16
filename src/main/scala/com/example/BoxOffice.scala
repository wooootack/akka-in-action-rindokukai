package com.example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext}


object BoxOffice {
  sealed trait Command

  case class CreateEvent(name: String, tickets: Int) extends Command
}

class BoxOffice(context: ActorContext[BoxOffice.Command])
  extends AbstractBehavior[BoxOffice.Command](context) {

  import BoxOffice._

  def createTicketSeller(name: String) =
    context.spawn()

  override def onMessage(msg: BoxOffice.Command): Behavior[BoxOffice.Command] =
    msg match {
      case CreateEvent(name, tickets) =>

    }
}
