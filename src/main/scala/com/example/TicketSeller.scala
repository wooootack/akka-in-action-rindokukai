package com.example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

object TicketSeller {
  sealed trait Command

  case class Ticket(id: Int)
  case class Add(tickets: Vector[Ticket]) extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup(context => new TicketSeller(context))
}

class TicketSeller(context: ActorContext[TicketSeller.Command])
  extends AbstractBehavior[TicketSeller.Command](context) {
  import TicketSeller._

  var tickets = Vector.empty[Ticket]

  override def onMessage(msg: TicketSeller.Command): Behavior[TicketSeller.Command] = {
    case Add(newTickets) =>
      tickets = tickets ++ newTickets
      Behaviors.same
  }
}
