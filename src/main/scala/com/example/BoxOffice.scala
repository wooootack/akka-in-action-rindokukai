package com.example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext}


object BoxOffice {
  sealed trait Command

  case class CreateEvent(name: String, tickets: Int, replyTo) extends Command

  sealed trait EventResponse
  case class EventCreated(event: Event) extends EventResponse
  case object EventExists extends EventResponse
}

class BoxOffice(context: ActorContext[BoxOffice.Command])
  extends AbstractBehavior[BoxOffice.Command](context) {

  import BoxOffice._

  def createTicketSeller(name: String) =
    context.spawn(TicketSeller(), name)

  override def onMessage(msg: BoxOffice.Command): Behavior[BoxOffice.Command] =
    msg match {
      case CreateEvent(name, tickets, replyTo) =>
        def create() = {
          val eventTickets = createTicketSeller(name)
          val newTickets = (1 to tickets).map { ticketId =>
            TicketSeller.Ticket(ticketId)
          }.toVector
        }
        context.child(name).fold(create()).(_ => replyTo ! )
    }
}
