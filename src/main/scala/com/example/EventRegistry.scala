package com.example

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import scala.collection.immutable


final case class Event(name: String, tickets: Int)
final case class Events(events: immutable.Seq[Event])

object EventRegistry {
  sealed trait Command
  final case class GetEvents(replyTo: ActorRef[Events]) extends Command
  final case class CreateEvent(name: String, replyTo: ActorRef[String]) extends Command

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(events: Set[Event]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetEvents(replyTo) =>
        replyTo ! Events(events.toSeq)
        Behaviors.same

      case CreateEvent(name, replyTo) =>
        replyTo ! name
        Behaviors.same
    }
}
