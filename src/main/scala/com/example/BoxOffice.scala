package com.example

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors} .Command

import scala.collection.immutable


object BoxOffice {
  sealed trait Command

  case class CreateEvent(name: String, tickets: Int, replyTo: ActorRef[EventResponse]) extends Command
  final case class GetEvents(replyTo: ActorRef[Events]) extends Command

  final case class Event(name: String, tickets: Int)
  final case class Events(events: immutable.Seq[Event])

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
          eventTickets ! TicketSeller.Add(newTickets)
          replyTo ! EventCreated(Event(name, tickets))
        }
        context.child(name).fold(create())(_ => replyTo ! EventExists)
        Behaviors.same
    }
}
