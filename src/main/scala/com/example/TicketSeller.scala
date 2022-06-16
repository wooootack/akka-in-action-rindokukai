package com.example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

object TicketSeller {
  sealed trait Command

  case class Ticket(id: Int)

  def apply(): Behavior[Command] =
    Behaviors.setup(context => new TicketSeller(context))
}

class TicketSeller(context: ActorContext[TicketSeller.Command])
  extends AbstractBehavior[TicketSeller.Command](context) {

  override def onMessage(msg: TicketSeller.Command): Behavior[TicketSeller.Command] =
    this
}
